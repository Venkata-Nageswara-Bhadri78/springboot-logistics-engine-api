package com.logistic.logistic_engine.dto.request;

import com.logistic.logistic_engine.enums.Priority;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {

    @NotBlank(message = "Pickup Address is Mandatory")
    private String pickupAddress;

    @NotBlank(message = "Delivery Address is Mandatory")
    private String deliveryAddress;

    @Positive
    @NotBlank(message = "Package Weight Must be Included")
    private Double packageWeight;

    @NotNull(message = "Priority is Mandatory")
    private Priority priority;
}
