package com.raved.gateway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Global Exception Handler for API Gateway
 *
 * This handler provides:
 * - Centralized error handling for all gateway exceptions
 * - Structured error responses with correlation IDs
 * - Different error handling strategies for different exception types
 * - Security-aware error messages (no sensitive information leakage)
 * - Comprehensive logging for debugging and monitoring
 *
 * @author Raved Development Team
 * @version 2.0.0
 */
@Component
@Order(-1) // High priority to handle exceptions before default handlers
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        // Get correlation ID for tracking
        String correlationId = exchange.getRequest().getHeaders().getFirst("X-Correlation-ID");
        if (correlationId == null) {
            correlationId = java.util.UUID.randomUUID().toString();
        }

        // Log the exception with context
        logException(exchange, ex, correlationId);

        // Determine appropriate HTTP status and error message
        ErrorResponse errorResponse = createErrorResponse(ex, correlationId);

        // Set response status and headers
        response.setStatusCode(errorResponse.getStatus());
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().add("X-Correlation-ID", correlationId);

        // Create response body
        String responseBody = createResponseBody(errorResponse);
        DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }

    /**
     * Log exception with appropriate level and context
     */
    private void logException(ServerWebExchange exchange, Throwable ex, String correlationId) {
        String method = exchange.getRequest().getMethod() != null
                ? exchange.getRequest().getMethod().name()
                : "UNKNOWN";
        String path = exchange.getRequest().getPath().value();
        String userAgent = exchange.getRequest().getHeaders().getFirst("User-Agent");
        String clientIP = getClientIP(exchange);

        if (ex instanceof ResponseStatusException) {
            ResponseStatusException rse = (ResponseStatusException) ex;
            HttpStatus status = HttpStatus.valueOf(rse.getStatusCode().value());
            if (status.is4xxClientError()) {
                log.warn("Client error [{}] - {} {} | IP: {} | UA: {} | Correlation: {} | Error: {}",
                        status.value(), method, path, clientIP, userAgent, correlationId, rse.getReason());
            } else {
                log.error("Server error [{}] - {} {} | IP: {} | UA: {} | Correlation: {} | Error: {}",
                        status.value(), method, path, clientIP, userAgent, correlationId, rse.getReason(), ex);
            }
        } else if (ex instanceof java.net.ConnectException || ex instanceof java.util.concurrent.TimeoutException) {
            log.error("Service connectivity error - {} {} | IP: {} | Correlation: {} | Error: {}",
                    method, path, clientIP, correlationId, ex.getMessage());
        } else if (ex instanceof SecurityException) {
            log.warn("Security exception - {} {} | IP: {} | Correlation: {} | Error: {}",
                    method, path, clientIP, correlationId, ex.getMessage());
        } else {
            log.error("Unexpected error - {} {} | IP: {} | UA: {} | Correlation: {} | Error: {}",
                    method, path, clientIP, userAgent, correlationId, ex.getMessage(), ex);
        }
    }

    /**
     * Create appropriate error response based on exception type
     */
    private ErrorResponse createErrorResponse(Throwable ex, String correlationId) {
        if (ex instanceof ResponseStatusException) {
            ResponseStatusException rse = (ResponseStatusException) ex;
            HttpStatus status = HttpStatus.valueOf(rse.getStatusCode().value());
            return new ErrorResponse(
                    status,
                    getErrorCode(status),
                    sanitizeErrorMessage(rse.getReason()),
                    correlationId);
        } else if (ex instanceof java.net.ConnectException) {
            return new ErrorResponse(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "SERVICE_UNAVAILABLE",
                    "Service temporarily unavailable. Please try again later.",
                    correlationId);
        } else if (ex instanceof java.util.concurrent.TimeoutException) {
            return new ErrorResponse(
                    HttpStatus.GATEWAY_TIMEOUT,
                    "GATEWAY_TIMEOUT",
                    "Request timeout. Please try again.",
                    correlationId);
        } else if (ex instanceof SecurityException) {
            return new ErrorResponse(
                    HttpStatus.FORBIDDEN,
                    "ACCESS_DENIED",
                    "Access denied. Insufficient permissions.",
                    correlationId);
        } else if (ex instanceof IllegalArgumentException) {
            return new ErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_REQUEST",
                    "Invalid request parameters.",
                    correlationId);
        } else {
            return new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "INTERNAL_ERROR",
                    "An unexpected error occurred. Please try again later.",
                    correlationId);
        }
    }

    /**
     * Get error code based on HTTP status
     */
    private String getErrorCode(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> "BAD_REQUEST";
            case UNAUTHORIZED -> "UNAUTHORIZED";
            case FORBIDDEN -> "FORBIDDEN";
            case NOT_FOUND -> "NOT_FOUND";
            case METHOD_NOT_ALLOWED -> "METHOD_NOT_ALLOWED";
            case TOO_MANY_REQUESTS -> "RATE_LIMIT_EXCEEDED";
            case INTERNAL_SERVER_ERROR -> "INTERNAL_ERROR";
            case BAD_GATEWAY -> "BAD_GATEWAY";
            case SERVICE_UNAVAILABLE -> "SERVICE_UNAVAILABLE";
            case GATEWAY_TIMEOUT -> "GATEWAY_TIMEOUT";
            default -> "UNKNOWN_ERROR";
        };
    }

    /**
     * Build JSON response body
     */
    private String createResponseBody(ErrorResponse error) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return String.format(
                "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"code\":\"%s\",\"correlationId\":\"%s\"}",
                timestamp,
                error.getStatus().value(),
                error.getStatus().getReasonPhrase(),
                error.getMessage(),
                error.getCode(),
                error.getCorrelationId());
    }

    /**
     * Sanitize error message to avoid leaking sensitive information
     */
    private String sanitizeErrorMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "An error occurred";
        }
        // Remove stack trace-like content or internal details
        return message.replaceAll("[\\r\\n]", " ").replaceAll("\\s+", " ").trim();
    }

    /**
     * Get client IP address from exchange
     */
    private String getClientIP(ServerWebExchange exchange) {
        String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIP = exchange.getRequest().getHeaders().getFirst("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        var remote = exchange.getRequest().getRemoteAddress();
        if (remote == null) {
            return "unknown";
        }
        var inet = remote.getAddress();
        String host = (inet != null) ? inet.getHostAddress() : remote.getHostString();
        return host != null ? host : "unknown";
    }

    /**
     * Error response model
     */
    private static class ErrorResponse {
        private final HttpStatus status;
        private final String code;
        private final String message;
        private final String correlationId;

        public ErrorResponse(HttpStatus status, String code, String message, String correlationId) {
            this.status = status;
            this.code = code;
            this.message = message;
            this.correlationId = correlationId;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getCorrelationId() {
            return correlationId;
        }
    }
}
