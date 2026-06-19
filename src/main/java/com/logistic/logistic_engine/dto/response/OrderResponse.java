package com.logistic.logistic_engine.dto.response;

import com.logistic.logistic_engine.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private OrderStatus status;
}
