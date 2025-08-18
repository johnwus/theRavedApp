package com.raved.social.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating an activity
 * Updated for MongoDB compatibility with String
 * IDs
 */
public class CreateActivityRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Activity type is required")
    private String activityType;

    private String targetUserId;
    private String postId;
    private String commentId;
    
    // Constructors
    public CreateActivityRequest() {
    }
    
    public CreateActivityRequest(String userId, String activityType, String targetUserId, String postId, String commentId) {
        this.userId = userId;
        this.activityType = activityType;
        this.targetUserId = targetUserId;
        this.postId = postId;
        this.commentId = commentId;
    }
    
    // Getters and Setters
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
}