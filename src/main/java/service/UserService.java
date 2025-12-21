package com.example.demo.service;

import com.example.demo.model.User;

import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> findByEmail(String email);
}