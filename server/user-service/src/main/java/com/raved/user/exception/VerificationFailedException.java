package com.raved.user.exception;

/**
 * Exception thrown when verification fails
 */
public class VerificationFailedException extends RuntimeException {

    public VerificationFailedException(String message) {
        super(message);
    }

    public VerificationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationFailedException(String type, String reason) {
        super(type + " verification failed: " + reason);
    }
}
