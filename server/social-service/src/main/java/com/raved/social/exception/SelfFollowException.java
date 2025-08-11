package com.raved.social.exception;

/**
 * Exception thrown when attempting to follow oneself
 */
public class SelfFollowException extends RuntimeException {
    
    public SelfFollowException(String message) {
        super(message);
    }
    
    public SelfFollowException(String message, Throwable cause) {
        super(message, cause);
    }
}
