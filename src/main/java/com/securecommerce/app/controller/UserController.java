package com.securecommerce.app.controller;


import com.securecommerce.app.dto.ApiResponse;
import com.securecommerce.app.dto.LoginRequest;
import com.securecommerce.app.dto.LoginResponse;
import com.securecommerce.app.entity.User;
import com.securecommerce.app.exception.InvalidCredentialsException;
import com.securecommerce.app.repository.UserRepository;
import com.securecommerce.app.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import com.securecommerce.app.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.securecommerce.app.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {

        logger.info("Login attempt for email: {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        logger.info("Login successful for email: {}", user.getEmail());

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return new ApiResponse<>(
                true,
                "Login successful",
                Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken
                )
        );
    }

    @PostMapping("/refresh")
    public ApiResponse<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        String email = jwtUtil.extractEmail(refreshToken);

        User user = userRepository.findByEmail(email);

        String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return new ApiResponse<>(
                true,
                "Token refreshed",
                Map.of("accessToken", newAccessToken)
        );
    }
}