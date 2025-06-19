package com.taichu.inventoryservice.service;

import com.taichu.inventoryservice.dto.InventoryResponse;
import com.taichu.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    // @Async
    // @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        // log.info("wait to started");
        // try {
        //     Thread.sleep(10000);
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }
        // log.info("wait ended");

        return inventoryRepository.findBySkuCodeIn(skuCode).stream().map(inventory ->
            InventoryResponse.builder()
                    .skuCode(inventory.getSkuCode())
                    .isInStock(inventory.getQuantity() > 0)
                    .build()
        ).toList();
    }
}
