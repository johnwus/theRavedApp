package com.raved.realtime.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a chat room is not found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChatRoomNotFoundException extends RuntimeException {

    public ChatRoomNotFoundException(String message) {
        super(message);
    }

    public ChatRoomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}