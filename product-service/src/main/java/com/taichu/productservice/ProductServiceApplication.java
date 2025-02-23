package com.taichu.productservice;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.taichu.productservice.model.Product;
import com.taichu.productservice.repository.ProductRepository;

@SpringBootApplication
@EnableEurekaClient
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(ProductRepository productRepository) {
		return arg -> {
			productRepository.save(new Product(1L,"iphone_13", "descriptio 1", BigDecimal.valueOf(100000)));
			productRepository.save(new Product(2L,"iphone_14", "description 2", BigDecimal.valueOf(200000)));
			productRepository.save(new Product(3L,"iphone_15", "description 3", BigDecimal.valueOf(300000)));
		};
	}

}
