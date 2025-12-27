package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User u) {
        if (u == null || u.getEmail() == null) throw new IllegalArgumentException("Email required");

        if (userRepository.findByEmail(u.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        u.setPassword(encoder.encode(u.getPassword()));
        return userRepository.save(u);
    }
}