package com.taichu.productservice.repository;

import com.taichu.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, Long> {
    List<Product> findByName (String productName);
}
