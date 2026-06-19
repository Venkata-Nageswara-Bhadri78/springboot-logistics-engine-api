package com.logistic.logistic_engine.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistic.logistic_engine.dto.request.LoginRequest;
import com.logistic.logistic_engine.dto.request.RegisterRequest;
import com.logistic.logistic_engine.dto.response.ApiResponse;
import com.logistic.logistic_engine.dto.response.LoginResponse;
import com.logistic.logistic_engine.dto.response.UserProfileResponse;
import com.logistic.logistic_engine.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        ApiResponse<String> response = new ApiResponse<>(
            true,
            "User registered successfully",
            null
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login (@Valid @RequestBody LoginRequest request) {
        LoginResponse data = authService.login(request);
        ApiResponse<LoginResponse> response = new ApiResponse<>(
            true,
            "Login Sucessful",
            data
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> me() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileResponse data = authService.getCurrentUser(email);
        
        ApiResponse<UserProfileResponse> response = new ApiResponse<>(
            true,
            "User profile fetched successfully",
            data
        );

        return ResponseEntity.ok(response);
    }
    
    
}
