package com.logistic.logistic_engine.dto.response;

import java.util.List;

import com.logistic.logistic_engine.entity.OrderHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderTimelineResponse {
    private List<OrderHistory> history;
}
