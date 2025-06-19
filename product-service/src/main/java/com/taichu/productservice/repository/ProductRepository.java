package com.taichu.productservice.repository;

import com.taichu.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findOneByName (String productName);
}
