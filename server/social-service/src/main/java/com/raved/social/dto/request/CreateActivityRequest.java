package com.raved.social.dto.request;

/**
 * DTO for creating an activity
 */
public class CreateActivityRequest {
    private Long userId;
    private String activityType;
    private Long targetUserId;
    private Long postId;
    private Long commentId;
    
    // Constructors
    public CreateActivityRequest() {
    }
    
    public CreateActivityRequest(Long userId, String activityType, Long targetUserId, Long postId, Long commentId) {
        this.userId = userId;
        this.activityType = activityType;
        this.targetUserId = targetUserId;
        this.postId = postId;
        this.commentId = commentId;
    }
    
    // Getters and Setters
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
}