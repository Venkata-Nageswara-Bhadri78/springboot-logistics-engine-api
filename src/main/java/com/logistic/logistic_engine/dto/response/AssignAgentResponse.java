package com.logistic.logistic_engine.dto.response;

import com.logistic.logistic_engine.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssignAgentResponse {
    private Long orderId;
    private Long agentId;
    private OrderStatus status;
}
