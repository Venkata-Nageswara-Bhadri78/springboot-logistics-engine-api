package com.logistic.logistic_engine.controller;

import com.logistic.logistic_engine.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return new ApiResponse<>(
                true,
                "Health check successful",
                "Logistic Engine API is running"
        );
    }
}
