package com.raved.social.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for Comment responses
 * Updated for MongoDB compatibility with String IDs
 */
public class CommentResponse {
    private String id;
    private String postId;
    private String userId;
    private String parentCommentId;
    private String content;
    private Integer likesCount;
    private Integer repliesCount;
    private Boolean isFlagged;
    private String moderationStatus;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for responses (not stored in database)
    private String userName;
    private String userAvatar;
    private Boolean isLikedByCurrentUser;
    private String flaggedReason; // Not stored in database, populated from other sources
    
    // Default constructor
    public CommentResponse() {
    }
    
    // Constructor with all fields
    public CommentResponse(String id, String postId, String userId, String parentCommentId, String content,
                         Integer likesCount, Integer repliesCount, Boolean isFlagged, String moderationStatus, 
                         Boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt, 
                         String userName, String userAvatar, Boolean isLikedByCurrentUser, String flaggedReason) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.likesCount = likesCount;
        this.repliesCount = repliesCount;
        this.isFlagged = isFlagged;
        this.moderationStatus = moderationStatus;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.isLikedByCurrentUser = isLikedByCurrentUser;
        this.flaggedReason = flaggedReason;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getPostId() {
        return postId;
    }
    
    public void setPostId(String postId) {
        this.postId = postId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getParentCommentId() {
        return parentCommentId;
    }
    
    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getLikesCount() {
        return likesCount;
    }
    
    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }
    
    public Integer getRepliesCount() {
        return repliesCount;
    }
    
    public void setRepliesCount(Integer repliesCount) {
        this.repliesCount = repliesCount;
    }
    
    public Boolean getIsFlagged() {
        return isFlagged;
    }
    
    public void setIsFlagged(Boolean isFlagged) {
        this.isFlagged = isFlagged;
    }
    
    public String getModerationStatus() {
        return moderationStatus;
    }
    
    public void setModerationStatus(String moderationStatus) {
        this.moderationStatus = moderationStatus;
    }
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
    
    public Boolean getIsLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }
    
    public void setIsLikedByCurrentUser(Boolean isLikedByCurrentUser) {
        this.isLikedByCurrentUser = isLikedByCurrentUser;
    }
    
    public String getFlaggedReason() {
        return flaggedReason;
    }
    
    public void setFlaggedReason(String flaggedReason) {
        this.flaggedReason = flaggedReason;
    }
}