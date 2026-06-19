package com.logistic.logistic_engine.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistic.logistic_engine.dto.request.OrderRequest;
import com.logistic.logistic_engine.dto.response.ApiResponse;
import com.logistic.logistic_engine.dto.response.LoginResponse;
import com.logistic.logistic_engine.dto.response.OrderResponse;
import com.logistic.logistic_engine.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> orders(@Valid @RequestBody OrderRequest orderRequest) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        OrderResponse data = orderService.postOrders(email, orderRequest);
        
        ApiResponse<OrderResponse> response = new ApiResponse<>(
            true,
            "Order Placed Sucessfully",
            data
        );

        return ResponseEntity.ok(response);
    }
    
}
