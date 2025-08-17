package com.raved.user.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, Object>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldError)
                .collect(Collectors.toList());

        Map<String, Object> error = new HashMap<>();
        error.put("code", "VALIDATION_ERROR");
        error.put("message", "Validation failed");
        error.put("details", fieldErrors);

        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("error", error);
        body.put("timestamp", Instant.now().toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex, WebRequest req) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        Map<String, Object> error = new HashMap<>();
        error.put("code", "INTERNAL_SERVER_ERROR");
        error.put("message", "Internal server error");

        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("error", error);
        body.put("timestamp", Instant.now().toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private Map<String, Object> toFieldError(FieldError fe) {
        Map<String, Object> m = new HashMap<>();
        m.put("field", fe.getField());
        m.put("message", fe.getDefaultMessage());
        return m;
    }
}

