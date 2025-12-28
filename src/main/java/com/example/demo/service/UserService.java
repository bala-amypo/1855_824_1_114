package com.example.demo.service;
import com.example.demo.entity.User;

public interface UserService {
    User register(User user);
    User findByEmail(String email); // Fixed: Added missing method
    User findById(Long id);
}