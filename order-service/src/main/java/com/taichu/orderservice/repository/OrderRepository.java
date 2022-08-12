package com.taichu.orderservice.repository;

import com.taichu.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrderRepository extends JpaRepository<Order, Long> {

}
