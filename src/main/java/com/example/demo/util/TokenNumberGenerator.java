package com.example.demo.util;

import java.util.UUID;

public class TokenNumberGenerator {
    public static String generate() {
        return "T-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}