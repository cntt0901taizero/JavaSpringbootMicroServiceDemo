package com.taichu.orderservice.service;

import com.taichu.orderservice.dto.InventoryResponse;
import com.taichu.orderservice.dto.OrderLineItemsDto;
import com.taichu.orderservice.dto.OrderRequest;
import com.taichu.orderservice.event.OrderPlacedEvent;
import com.taichu.orderservice.model.Order;
import com.taichu.orderservice.model.OrderLineItems;
import com.taichu.orderservice.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    // public static final int CORE_POOL_SIZE = 0;
    // public static final int MAXIMUM_POOL_SIZE = 10;
    // public ThreadPoolExecutor thread() {
    //     final AtomicLong count = new AtomicLong(0);
    //     return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, 
    //     new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
    //         @Override
    //         public Thread newThread(Runnable r) {
    //             Thread t = new Thread(r);
    //             String threadName = "gp-" + count.getAndIncrement();
    //             t.setName(threadName);
    //             return t;
    //         }
    //     });
    // }

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackPlaceOrder")
    @TimeLimiter(name = "inventory")
    public CompletableFuture<String> placeOrder(OrderRequest orderRequest) {

        // CompletableFuture<String> completableFuture1 = new CompletableFuture<>();
        // completableFuture1.complete("abc");
        // CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> {
        //     return "abc";
        // });
        // CompletableFuture<Void> completableFuture3 = CompletableFuture.allOf(completableFuture1, completableFuture2);
        // completableFuture3.thenAccept(a -> {
        //     String resultA = completableFuture1.get();
        //     String resultB = completableFuture2.get();
        // });

        return CompletableFuture.supplyAsync(() -> {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                    .stream()
                    .map(this::mapToOrderLineItems)
                    .toList();

            order.setOrderLineItemsList(orderLineItemsList);

            List<String> skuCodeList = order.getOrderLineItemsList().stream()
                    .map(OrderLineItems::getSkuCode).toList();

            // Vi dung circuitbreaker nen khi goi inventory-service se tao new theard (khong
            // cung trace id)
            // => can tao 1 SpanId lookup cho theard nay
            Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
            try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {
                InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get() // call inventory- service
                        .uri("http://inventory-service/api/inventory/is-in-stock",
                                uriBuilder -> uriBuilder.queryParam("skuCode", skuCodeList).build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();

                assert inventoryResponsesArray != null;
                boolean isAllMatch = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::getIsInStock);

                if (isAllMatch) {
                    orderRepository.save(order);
                    // Send noti by kafka event
                    // kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                    return "Order Placed Successfully";
                } else {
                    throw new IllegalArgumentException("Product is not in stock, please try again later");
                }
            } finally {
                inventoryServiceLookup.end();
            }
        });
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto item) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(item.getPrice());
        orderLineItems.setQuantity(item.getQuantity());
        orderLineItems.setSkuCode(item.getSkuCode());
        return orderLineItems;
    }

    public CompletableFuture<String> fallbackPlaceOrder(OrderRequest orderRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Đã có lỗi xảy ra: " + runtimeException.getMessage());
    }
}
