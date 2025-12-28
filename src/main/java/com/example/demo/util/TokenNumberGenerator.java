package com.example.demo.util;
import java.util.UUID;

public class TokenNumberGenerator {
    // FIX: Changed to accept NO arguments to match TokenServiceImpl call
    public static String generate() {
        return "TKN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}