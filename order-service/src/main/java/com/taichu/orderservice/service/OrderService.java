package com.taichu.orderservice.service;

import com.taichu.orderservice.dto.InventoryResponse;
import com.taichu.orderservice.dto.OrderLineItemsDto;
import com.taichu.orderservice.dto.OrderRequest;
import com.taichu.orderservice.model.Order;
import com.taichu.orderservice.model.OrderLineItems;
import com.taichu.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToOrderLineItems)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodeList = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode).collect(Collectors.toList());

        InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder ->
                        uriBuilder.queryParam("skuCode", skuCodeList).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        assert inventoryResponsesArray != null;
        boolean isAllMatch = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::getIsInStock);

        if (isAllMatch) {
            orderRepository.save(order);
            return "Order Placed Successfully";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto item) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(item.getPrice());
        orderLineItems.setQuantity(item.getQuantity());
        orderLineItems.setSkuCode(item.getSkuCode());
        return orderLineItems;
    }
}
