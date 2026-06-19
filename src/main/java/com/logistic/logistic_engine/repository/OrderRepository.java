package com.logistic.logistic_engine.repository;

import com.logistic.logistic_engine.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}