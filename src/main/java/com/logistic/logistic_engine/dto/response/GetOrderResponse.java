package com.logistic.logistic_engine.dto.response;

import java.util.List;

import com.logistic.logistic_engine.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderResponse {
    private List<Order> order;
    private PaginatedOrderResponse paginatedOrderResponse;

}
