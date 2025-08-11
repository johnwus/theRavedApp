package com.raved.notification.exception;

/**
 * TemplateNotFoundException for TheRavedApp
 */
public class TemplateNotFoundException extends RuntimeException {
    
    public TemplateNotFoundException() {
        super();
    }
    
    public TemplateNotFoundException(String message) {
        super(message);
    }
    
    public TemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
