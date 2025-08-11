package com.raved.analytics.exception;

/**
 * AnalyticsProcessingException for TheRavedApp
 */
public class AnalyticsProcessingException extends RuntimeException {
    
    public AnalyticsProcessingException(String message) {
        super(message);
    }
    
    public AnalyticsProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
