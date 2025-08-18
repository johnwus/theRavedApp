package com.raved.social.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for Like responses
 * Updated for MongoDB compatibility with String IDs
 */
public class LikeResponse {
    private String id;
    private String userId;
    private String targetId;
    private String targetType;
    private LocalDateTime createdAt;
    
    // Additional fields for responses
    private String userName;
    private String userAvatar;
    
    // Default constructor
    public LikeResponse() {
    }
    
    // Constructor with all fields
    public LikeResponse(String id, String userId, String targetId, String targetType, LocalDateTime createdAt, String userName, String userAvatar) {
        this.id = id;
        this.userId = userId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.createdAt = createdAt;
        this.userName = userName;
        this.userAvatar = userAvatar;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserAvatar() {
        return userAvatar;
    }
    
    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}