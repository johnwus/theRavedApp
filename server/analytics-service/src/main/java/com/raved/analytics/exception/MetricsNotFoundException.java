package com.raved.analytics.exception;

/**
 * MetricsNotFoundException for TheRavedApp
 */
public class MetricsNotFoundException extends RuntimeException {
    public MetricsNotFoundException() {
        super();
    }

    public MetricsNotFoundException(String message) {
        super(message);
    }

    public MetricsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
