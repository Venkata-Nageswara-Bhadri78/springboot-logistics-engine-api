package com.logistic.logistic_engine.dto.Params;

import com.logistic.logistic_engine.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderParams {
    private Long page;
    private Long limit;
    private OrderStatus orderStatus;
    private Long agentId;
    private Long customerId;

}
