package com.example.demo.util;
import java.util.UUID;

public class TokenNumberGenerator {
    public static String generate() {
        return "TKN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}