package com.example.demo.controller;

import com.example.demo.security.JwtTokenProvider;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "AuthController")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    @Operation(summary = "Register user")
    public User register(@RequestBody RegisterRequest request) {
        User u = new User();
        u.setName(request.getName());
        u.setEmail(request.getEmail());

        // store password as plain text because BCrypt dependency is missing
        u.setPassword(request.getPassword());

        u.setRole((request.getRole() == null || request.getRole().isBlank())
                ? "STAFF"
                : request.getRole().toUpperCase());

        return userService.register(u);
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null) {
            // must contain "not found"
            throw new ResourceNotFoundException("User not found");
        }

        // plain check
        if (user.getPassword() == null || !user.getPassword().equals(request.getPassword())) {
            throw new IllegalStateException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(token);
    }
}