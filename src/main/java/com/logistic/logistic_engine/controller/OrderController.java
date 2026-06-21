package com.logistic.logistic_engine.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistic.logistic_engine.dto.Params.OrderParams;
import com.logistic.logistic_engine.dto.request.AssignAgentRequest;
import com.logistic.logistic_engine.dto.request.CreateOrderRequest;
import com.logistic.logistic_engine.dto.response.ApiResponse;
import com.logistic.logistic_engine.dto.response.AssignAgentResponse;
import com.logistic.logistic_engine.dto.response.LoginResponse;
import com.logistic.logistic_engine.dto.response.PaginatedOrderResponse;
import com.logistic.logistic_engine.enums.OrderStatus;
import com.logistic.logistic_engine.repository.UserRepository;
import com.logistic.logistic_engine.dto.response.CreateOrderResponse;
import com.logistic.logistic_engine.dto.response.GetOrderResponse;
import com.logistic.logistic_engine.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        CreateOrderResponse data = orderService.postOrders(email, orderRequest);
        
        ApiResponse<CreateOrderResponse> response = new ApiResponse<>(
            true,
            "Order Placed Sucessfully",
            data
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<GetOrderResponse>> getOrders(
        @RequestParam(defaultValue = "1") Long page, 
        @RequestParam(defaultValue = "10") Long limit, 
        @RequestParam(required = false) OrderStatus status,
        @RequestParam(required = false) Long agentId,
        @RequestParam(required = false) Long customerId
    ) {
        String email = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        
        OrderParams orderParams = new OrderParams(page, limit, status, agentId, customerId);
        GetOrderResponse data = orderService.getOrders(email, orderParams);
        ApiResponse<GetOrderResponse> response = new ApiResponse<>(
            true,
            "Orders fetched successfully",
            data
        );
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{orderId}/assign")
    public ResponseEntity<ApiResponse<AssignAgentResponse>> assignAgent(
        @PathVariable Long orderId, 
        @RequestBody AssignAgentRequest request
    ) {
        String email = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        
        AssignAgentResponse data = orderService.assignAgent(email, orderId, request);

        ApiResponse<AssignAgentResponse> response = new ApiResponse<>(
            true,
            "Assign Agent Sucessfully",
            data
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/start-delivery")
    public CreateOrderResponse startOrderDelivery(@PathVariable Long orderId) {
        String email = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        CreateOrderResponse data = orderService.startOrderDelivery(email, orderId);
        
        return null;
    }
    
    
}
