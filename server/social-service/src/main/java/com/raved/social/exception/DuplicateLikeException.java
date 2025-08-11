package com.raved.social.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user tries to like a post or comment they have already liked
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateLikeException extends RuntimeException {
    
    public DuplicateLikeException(String message) {
        super(message);
    }
}