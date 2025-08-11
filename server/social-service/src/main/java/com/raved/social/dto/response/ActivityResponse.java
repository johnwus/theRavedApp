package com.raved.social.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for Activity responses
 */
public class ActivityResponse {
    private Long id;
    private Long userId;
    private String activityType;
    private Long targetUserId;
    private Long postId;
    private Long commentId;
    private LocalDateTime createdAt;
    
    // Default constructor
    public ActivityResponse() {
    }
    
    // Constructor with all fields
    public ActivityResponse(Long id, Long userId, String activityType, Long targetUserId, Long postId, Long commentId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.activityType = activityType;
        this.targetUserId = targetUserId;
        this.postId = postId;
        this.commentId = commentId;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    
    public Long getTargetUserId() {
        return targetUserId;
    }
    
    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }
    
    public Long getPostId() {
        return postId;
    }
    
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public Long getCommentId() {
        return commentId;
    }
    
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}