package com.logistic.logistic_engine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.query.Page;
import org.springframework.stereotype.Service;

import com.logistic.logistic_engine.dto.Params.OrderParams;
import com.logistic.logistic_engine.dto.request.AssignAgentRequest;
import com.logistic.logistic_engine.dto.request.CreateOrderRequest;
import com.logistic.logistic_engine.dto.response.AssignAgentResponse;
import com.logistic.logistic_engine.dto.response.CreateOrderResponse;
import com.logistic.logistic_engine.dto.response.GetOrderResponse;
import com.logistic.logistic_engine.dto.response.PaginatedOrderResponse;
import com.logistic.logistic_engine.entity.Agent;
import com.logistic.logistic_engine.entity.Customer;
import com.logistic.logistic_engine.entity.Order;
import com.logistic.logistic_engine.entity.OrderHistory;
import com.logistic.logistic_engine.entity.OrderHistory.OrderHistoryBuilder;
import com.logistic.logistic_engine.entity.User;
import com.logistic.logistic_engine.enums.OrderStatus;
import com.logistic.logistic_engine.enums.Role;
import com.logistic.logistic_engine.repository.AgentRepository;
import com.logistic.logistic_engine.repository.CustomerRepository;
import com.logistic.logistic_engine.repository.OrderHistoryRepository;
import com.logistic.logistic_engine.repository.OrderRepository;
import com.logistic.logistic_engine.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    public CreateOrderResponse postOrders(String email, CreateOrderRequest orderRequest){

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

        return new CreateOrderResponse(
            savedOrder.getId(),
            savedOrder.getStatus()
        );
        
    }
    
    public GetOrderResponse getOrders(String email, OrderParams orderParams){
        User user = userRepository.findByEmail(email)
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "User not found"
                            )
                    );
        // Place this right under: Long offset = (orderParams.getPage()-1) * orderParams.getLimit();
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(orderParams.getPage().intValue() - 1, orderParams.getLimit().intValue());
        
        Long offset = (orderParams.getPage()-1) * orderParams.getLimit();
        org.springframework.data.domain.Page<Order> orderPage;

        if(Objects.nonNull(orderParams.getAgentId())){
            // Agent Code Here
            orderPage = (org.springframework.data.domain.Page<Order>) orderRepository.findAllCustomOrdersByAgentId(orderParams.getAgentId(), orderParams.getOrderStatus(), offset, orderParams.getLimit());
        }
        else if(Objects.nonNull(orderParams.getCustomerId())){
            // Customer Code
            orderPage = (org.springframework.data.domain.Page<Order>) orderRepository.findAllCustomOrdersByCustomerId(orderParams.getCustomerId(), orderParams.getOrderStatus(), offset, orderParams.getLimit());
        }
        else{
            // Admin Code
            // orderPage = (org.springframework.data.domain.Page<Order>) orderRepository.findAll();
            orderPage = orderRepository.findAll(pageable);
        }
        List<Order> ordersList = orderPage.getContent();
        Long total = orderPage.getTotalElements();

        PaginatedOrderResponse paginatedOrderResponse = new PaginatedOrderResponse(orderParams.getPage(), orderParams.getLimit(), total);
        return new GetOrderResponse(ordersList, paginatedOrderResponse);
    }

    @Transactional
    public AssignAgentResponse assignAgent(String email, Long orderId, AssignAgentRequest request){
        User admin = userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                    "User not found"
                                )
                        );

        if(admin.getRole() != Role.ADMIN){
            throw new RuntimeException(
                    "Only admin can assign agent"
            );
        }

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Order not found"
                        )
                );
        if(order.getStatus() != OrderStatus.CREATED){

            throw new RuntimeException(
                    "Order already assigned"
            );
        
        }

        Agent agent = agentRepository
                .findById(
                        request.getAgentId()
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Agent not found"
                        )
                );
        
        order.setAgent(agent);

        order.setStatus(
                OrderStatus.ASSIGNED
        );
        orderRepository.save(order);

        OrderHistory history = OrderHistory.builder()
                .order(order)
                .previousStatus(OrderStatus.CREATED)
                .newStatus(OrderStatus.ASSIGNED)
                .changedBy(admin)
                .note("Agent assigned")
                .build();
        
        orderHistoryRepository.save(history);
        
        return new AssignAgentResponse(
            order.getId(),
            agent.getId(),
            order.getStatus()
        );
    }

    public CreateOrderResponse startOrderDelivery(String email, Long orderId){
        User admin = userRepository
            .findByEmail(email)
            .orElseThrow(
                    () -> new RuntimeException(
                            "User not found"
                    )
            );
        
        Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> 
                            new RuntimeException("Order Doesn't Exist")
                        );
        
        if(admin.getRole() != Role.AGENT){
            throw new RuntimeException(
                    "Only agent can start delivery"
            );
        }

        if(order.getStatus() != OrderStatus.ASSIGNED){
            throw new RuntimeException(
                    "Agent can't start delivery"
            );
        }

        if(admin.getId()!=orderRepository.findAgentIdByOrderId(order.getId())){
            throw new RuntimeException(
                "You can't start delivery of another agent's order"
            );
        }
        order.setStatus(OrderStatus.IN_TRANSIT);
        orderRepository.save(order);

        OrderHistory history = OrderHistory.builder()
                .order(order)
                .previousStatus(OrderStatus.ASSIGNED)
                .newStatus(OrderStatus.IN_TRANSIT)
                .changedBy(admin)
                .note("Order Started to Delivery")
                .build();

        orderHistoryRepository.save(history);

        CreateOrderResponse orderResponse = new CreateOrderResponse(order.getId(), order.getStatus());

        return orderResponse;
    }

    public CreateOrderResponse completeOrderDelivery(String email, Long orderId){
        User admin = userRepository
            .findByEmail(email)
            .orElseThrow(
                    () -> new RuntimeException(
                            "User not found"
                    )
            );
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> 
                new RuntimeException("Order Doesn't Exist")
            );
    
        if(admin.getRole() != Role.AGENT){
            throw new RuntimeException(
                    "Only agent can complete the order delivery"
            );
        }

        if(order.getStatus() != OrderStatus.IN_TRANSIT){
            throw new RuntimeException(
                    "Agent can't complete this delivery"
            );
        }

        if(admin.getId()!=orderRepository.findAgentIdByOrderId(order.getId())){
            throw new RuntimeException(
                "You can't complete the order delivery of another agent's order"
            );
        }

        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);

        OrderHistory history = OrderHistory.builder()
                .order(order)
                .previousStatus(OrderStatus.IN_TRANSIT)
                .newStatus(OrderStatus.DELIVERED)
                .changedBy(admin)
                .note("Order Deliveried Sucessfully")
                .build();
        
        orderHistoryRepository.save(history);

        CreateOrderResponse orderResponse = new CreateOrderResponse(order.getId(), order.getStatus());
        return orderResponse;
    }

    public CreateOrderResponse failedOrderDelivery(String email, Long orderId, String reason){
        User agent = userRepository
            .findByEmail(email)
            .orElseThrow(
                    () -> new RuntimeException(
                            "User not found"
                    )
            );
            
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> 
                new RuntimeException("Order Doesn't Exist")
            );
        
        if(agent.getRole()!=Role.AGENT){
            throw new RuntimeException("Access Denied");
        }
        
        if(agent.getId() != order.getAgent().getId()){
            throw new RuntimeException("You don't have access to this order");
        }
        if(order.getStatus() != OrderStatus.IN_TRANSIT){
            throw new RuntimeException("Order Status Invalid");
        }
        order.setStatus(OrderStatus.FAILED);
        orderRepository.save(order);

        OrderHistory history = OrderHistory.builder()
                                .order(order)
                                .previousStatus(OrderStatus.IN_TRANSIT)
                                .newStatus(OrderStatus.FAILED)
                                .changedBy(agent)
                                .note(reason)
                                .build();
        orderHistoryRepository.save(history);

        return new CreateOrderResponse(orderId, order.getStatus());
    }

    public CreateOrderResponse retryOrderDelivery(String email, Long orderId){
        User admin = userRepository
            .findByEmail(email)
            .orElseThrow(
                    () -> new RuntimeException(
                            "User not found"
                    )
            );
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> 
                new RuntimeException("Order Doesn't Exist")
            );
        
        if(admin.getRole()!=Role.ADMIN){
            throw new RuntimeException("Access Denied");
        }

        if(order.getStatus() != OrderStatus.FAILED){
            throw new RuntimeException("Order Status Invalid");
        }

        order.setStatus(OrderStatus.ASSIGNED);
        orderRepository.save(order);

        OrderHistory history = OrderHistory.builder()
                                .order(order)
                                .previousStatus(OrderStatus.FAILED)
                                .newStatus(OrderStatus.ASSIGNED)
                                .changedBy(admin)
                                .note("Order retried for delivery")
                                .build();
        orderHistoryRepository.save(history);

        return new CreateOrderResponse(orderId, order.getStatus());
    }
}
