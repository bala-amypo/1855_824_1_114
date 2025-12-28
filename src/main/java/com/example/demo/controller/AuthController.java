package com.example.demo.controller;
import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest req) {
        User u = new User();
        u.setEmail(req.getEmail());
        u.setPassword(req.getPassword());
        u.setRole(req.getRole());
        return userService.register(u);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        // In real app, authenticate via AuthManager first
        return new AuthResponse(jwtTokenProvider.generateToken(1L, req.getEmail(), "USER"), 1L, req.getEmail(), "USER");
    }
}