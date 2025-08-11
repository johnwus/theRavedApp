package com.raved.common.exception;

/**
 * ServiceException for TheRavedApp
 */
public class ServiceException extends BaseException {
    
    public ServiceException(String message) {
        super(message, "SERVICE_ERROR", 500);
    }
    
    public ServiceException(String message, String errorCode) {
        super(message, errorCode, 500);
    }
    
    public ServiceException(String message, String errorCode, int httpStatus) {
        super(message, errorCode, httpStatus);
    }
    
    public ServiceException(String message, String errorCode, int httpStatus, Throwable cause) {
        super(message, errorCode, httpStatus, cause);
    }
}
