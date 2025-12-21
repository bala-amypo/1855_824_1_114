package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class TokenNumberGenerator {

    private final AtomicLong counter = new AtomicLong(1000);

    public long nextToken() {
        return counter.incrementAndGet();
    }
}