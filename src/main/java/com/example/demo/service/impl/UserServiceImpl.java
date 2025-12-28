package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository repo;

    public User register(User u) {
        Optional<User> existing = repo.findByEmail(u.getEmail());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        // Simulate encoding
        u.setPassword("encoded_" + u.getPassword());
        return repo.save(u);
    }
}