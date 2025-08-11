package com.raved.social.dto.request;

/**
 * DTO for like requests
 */
public class LikeRequest {
    private Long userId;
    private Long targetId;
    private String targetType; // "POST", "COMMENT", or "PRODUCT"
    
    // Default constructor
    public LikeRequest() {
    }
    
    // Constructor with all fields
    public LikeRequest(Long userId, Long targetId, String targetType) {
        this.userId = userId;
        this.targetId = targetId;
        this.targetType = targetType;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getTargetId() {
        return targetId;
    }
    
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}