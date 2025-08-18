package com.raved.notification.exception;

/**
 * Exception thrown when there's an error processing a template
 */
public class TemplateProcessingException extends RuntimeException {

    public TemplateProcessingException(String message) {
        super(message);
    }

    public TemplateProcessingException(String templateName, Throwable cause) {
        super("Error processing template: " + templateName, cause);
    }
}
