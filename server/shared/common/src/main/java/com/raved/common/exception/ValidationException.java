package com.raved.common.exception;

/**
 * ValidationException for TheRavedApp
 */
public class ValidationException extends BaseException {
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR", 400);
    }
    
    public ValidationException(String message, String errorCode) {
        super(message, errorCode, 400);
    }
    
    public ValidationException(String message, String errorCode, int httpStatus) {
        super(message, errorCode, httpStatus);
    }
    
    public ValidationException(String message, String errorCode, int httpStatus, Throwable cause) {
        super(message, errorCode, httpStatus, cause);
    }
}
