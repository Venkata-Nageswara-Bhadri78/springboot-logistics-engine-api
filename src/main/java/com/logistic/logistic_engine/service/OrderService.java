package com.logistic.logistic_engine.service;

import org.springframework.stereotype.Service;

import com.logistic.logistic_engine.dto.request.OrderRequest;
import com.logistic.logistic_engine.dto.response.OrderResponse;
import com.logistic.logistic_engine.entity.Customer;
import com.logistic.logistic_engine.entity.Order;
import com.logistic.logistic_engine.entity.User;
import com.logistic.logistic_engine.enums.OrderStatus;
import com.logistic.logistic_engine.repository.CustomerRepository;
import com.logistic.logistic_engine.repository.OrderRepository;
import com.logistic.logistic_engine.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderResponse postOrders(String email, OrderRequest orderRequest){

        User user =
                userRepository.findByEmail(email)
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "User not found"
                            )
                    );
        
        Customer customer =
                customerRepository.findByUserId(
                            user.getId()
                    )
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Customer not found"
                            )
                    );
                    
        Order order = new Order();

        order.setCustomer(customer);

        order.setPickupAddress(
            orderRequest.getPickupAddress()
        );

        order.setDeliveryAddress(
            orderRequest.getDeliveryAddress()
        );

        order.setPackageWeight(
            orderRequest.getPackageWeight()
        );

        order.setPriority(
            orderRequest.getPriority()
        );

        order.setStatus(
                OrderStatus.CREATED
        );

        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
            savedOrder.getId(),
            savedOrder.getStatus()
        );
        
    }
}
