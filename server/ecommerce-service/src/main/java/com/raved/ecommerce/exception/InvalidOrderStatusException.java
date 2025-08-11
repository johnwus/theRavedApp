package com.raved.ecommerce.exception;

/**
 * Exception thrown when an invalid order status operation is attempted
 */
public class InvalidOrderStatusException extends RuntimeException {

    public InvalidOrderStatusException(String message) {
        super(message);
    }

    public InvalidOrderStatusException(String message, Throwable cause) {
        super(message, cause);
    }
} 