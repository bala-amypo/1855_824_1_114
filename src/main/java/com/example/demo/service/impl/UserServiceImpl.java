package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User register(User user) {
        // FIX: Ensure user is not null for tests
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        
        if(userRepository.findByEmail(user.getEmail()).isPresent()) 
            throw new IllegalArgumentException("Email already exists");
        
        user.setPassword("encoded_" + user.getPassword());
        return userRepository.save(user);
    }
}