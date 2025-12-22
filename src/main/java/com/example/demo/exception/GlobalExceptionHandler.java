// package com.example.demo.exception;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// import java.util.HashMap;
// import java.util.Map;

// @RestControllerAdvice
// public class GlobalExceptionHandler {

//     @ExceptionHandler(ResourceNotFoundException.class)
//     public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
//         Map<String, String> res = new HashMap<>();
//         res.put("error", ex.getMessage());
//         return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
//     }

//     @ExceptionHandler(IllegalArgumentException.class)
//     public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
//         Map<String, String> res = new HashMap<>();
//         res.put("error", ex.getMessage());
//         return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
//     }
// }