package com.logistic.logistic_engine.service;

import com.logistic.logistic_engine.dto.request.LoginRequest;
import com.logistic.logistic_engine.dto.request.RegisterRequest;
import com.logistic.logistic_engine.dto.response.LoginResponse;
import com.logistic.logistic_engine.dto.response.UserProfileResponse;
import com.logistic.logistic_engine.entity.Agent;
import com.logistic.logistic_engine.entity.Customer;
import com.logistic.logistic_engine.entity.User;
import com.logistic.logistic_engine.enums.Role;
import com.logistic.logistic_engine.exception.EmailAlreadyExistsException;
import com.logistic.logistic_engine.repository.AgentRepository;
import com.logistic.logistic_engine.repository.CustomerRepository;
import com.logistic.logistic_engine.repository.UserRepository;
import com.logistic.logistic_engine.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already exists"
            );
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        if (request.getRole() == Role.CUSTOMER) {
            Customer customer = Customer.builder()
                    .user(savedUser)
                    .build();

            customerRepository.save(customer);
        }

        if (request.getRole() == Role.AGENT) {
            Agent agent = Agent.builder()
                    .user(savedUser)
                    .build();

            agentRepository.save(agent);
        }
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        String token =
        jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
        
        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }

    public UserProfileResponse getCurrentUser(String email){
        User user = userRepository.findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException("User Not Found")
                        );
        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}