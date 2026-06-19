package com.logistic.logistic_engine.dto.response;

import com.logistic.logistic_engine.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;

    private String name;

    private String email;

    private Role role;
}
