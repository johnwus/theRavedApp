package com.raved.content.exception;

/**
 * PostNotFoundException for TheRavedApp
 */
public class PostNotFoundException extends RuntimeException {
    
    public PostNotFoundException() {
        super();
    }
    
    public PostNotFoundException(String message) {
        super(message);
    }
    
    public PostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
