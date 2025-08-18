package com.raved.social.exception;

/**
 * Exception thrown when attempting to follow a user that is already being followed
 */
public class DuplicateFollowException extends RuntimeException {
    
    public DuplicateFollowException(String message) {
        super(message);
    }
    
    public DuplicateFollowException(String message, Throwable cause) {
        super(message, cause);
    }
}
