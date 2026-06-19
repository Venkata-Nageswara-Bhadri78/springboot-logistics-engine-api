package com.logistic.logistic_engine.repository;

import com.logistic.logistic_engine.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    List<OrderHistory> findByOrderIdOrderByCreatedAtAsc(Long orderId);
}