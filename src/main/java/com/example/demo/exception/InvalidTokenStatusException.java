package com.example.demo.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTokenStatusException extends IllegalArgumentException {
    public InvalidTokenStatusException(String s) { super(s); }
}