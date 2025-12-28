package com.example.demo.util;
import java.util.UUID;

public class TokenNumberGenerator {
    // FIX: Removed arguments to match the Service call
    public static String generate() {
        return "TKN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}