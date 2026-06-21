package com.logistic.logistic_engine.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailOrderRequest {
    @NotBlank
    private String reason;
}
