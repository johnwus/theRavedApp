package com.raved.common.dto;

import java.time.LocalDateTime;

/**
 * Base response DTO for all API responses
 */
public class BaseResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String path;

    public BaseResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public BaseResponse(boolean success, String message, T data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, "Success", data);
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(true, message, data);
    }

    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(false, message, null);
    }

    public static <T> BaseResponse<T> error(String message, T data) {
        return new BaseResponse<>(false, message, data);
    }

    public static BaseResponse<Void> ok() {
        return new BaseResponse<>(true, "Operation completed successfully", null);
    }

    public static BaseResponse<Void> ok(String message) {
        return new BaseResponse<>(true, message, null);
    }

    public static BaseResponse<Void> created() {
        return new BaseResponse<>(true, "Resource created successfully", null);
    }

    public static <T> BaseResponse<T> created(T data) {
        return new BaseResponse<>(true, "Resource created successfully", data);
    }

    public static BaseResponse<Void> updated() {
        return new BaseResponse<>(true, "Resource updated successfully", null);
    }

    public static BaseResponse<Void> deleted() {
        return new BaseResponse<>(true, "Resource deleted successfully", null);
    }

    public static BaseResponse<Void> notFound(String resource) {
        return new BaseResponse<>(false, resource + " not found", null);
    }

    public static BaseResponse<Void> unauthorized() {
        return new BaseResponse<>(false, "Unauthorized access", null);
    }

    public static BaseResponse<Void> forbidden() {
        return new BaseResponse<>(false, "Access forbidden", null);
    }

    public static BaseResponse<Void> badRequest(String message) {
        return new BaseResponse<>(false, "Bad request: " + message, null);
    }

    public BaseResponse<T> withPath(String path) {
        this.path = path;
        return this;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
