package com.raved.social.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for like requests
 * Updated for MongoDB compatibility with String IDs
 */
public class LikeRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Target ID is required")
    private String targetId;

    @NotBlank(message = "Target type is required")
    private String targetType; // "POST", "COMMENT", or "PRODUCT"
    
    // Default constructor
    public LikeRequest() {
    }
    
    // Constructor with all fields
    public LikeRequest(String userId, String targetId, String targetType) {
        this.userId = userId;
        this.targetId = targetId;
        this.targetType = targetType;
    }
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getTargetId() {
        return targetId;
    }
    
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}