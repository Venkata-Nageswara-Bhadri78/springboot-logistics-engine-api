package com.logistic.logistic_engine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginatedOrderResponse {
    private Long page;
    private Long limit;
    private Long total;
}
