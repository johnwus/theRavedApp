package com.raved.social.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for Activity responses
 * Updated for MongoDB compatibility with String IDs
 */
public class ActivityResponse {
    private String id;
    private String userId;
    private String activityType;
    private String targetUserId;
    private String postId;
    private String commentId;
    private LocalDateTime createdAt;
    
    // Default constructor
    public ActivityResponse() {
    }
    
    // Constructor with all fields
    public ActivityResponse(String id, String userId, String activityType, String targetUserId, String postId, String commentId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.activityType = activityType;
        this.targetUserId = targetUserId;
        this.postId = postId;
        this.commentId = commentId;
        this.createdAt = createdAt;
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
    
    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}