package com.taichu.inventoryservice;

import com.taichu.inventoryservice.model.Inventory;
import com.taichu.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

// 	@Bean
// 	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
// 		return arg -> {
// //			Inventory inventory	= new Inventory();
// //			inventory.setSkuCode("iphone_13");
// //			inventory.setQuantity(100);
// //
// //			Inventory inventory1 = new Inventory();
// //			inventory.setSkuCode("iphone_13_red");
// //			inventory.setQuantity(0);

// 			inventoryRepository.save(new Inventory(1L,"iphone_13", 100));
// 			inventoryRepository.save(new Inventory(2L,"iphone_13_red", 0));
// 		};
// 	}
}
