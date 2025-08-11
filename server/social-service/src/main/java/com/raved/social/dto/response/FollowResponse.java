package com.raved.social.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for Follow responses
 */
public class FollowResponse {
    private Long id;
    private Long followerId;
    private Long followingId;
    private LocalDateTime createdAt;
    
    // Additional fields for responses
    private String followerName;
    private String followerAvatar;
    private String followingName;
    private String followingAvatar;
    private Boolean isFollowingBack;
    
    // Default constructor
    public FollowResponse() {
    }
    
    // Constructor with all fields
    public FollowResponse(Long id, Long followerId, Long followingId, LocalDateTime createdAt, 
                        String followerName, String followerAvatar, String followingName, 
                        String followingAvatar, Boolean isFollowingBack) {
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
        this.createdAt = createdAt;
        this.followerName = followerName;
        this.followerAvatar = followerAvatar;
        this.followingName = followingName;
        this.followingAvatar = followingAvatar;
        this.isFollowingBack = isFollowingBack;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFollowerId() {
        return followerId;
    }
    
    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }
    
    public Long getFollowingId() {
        return followingId;
    }
    
    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getFollowerName() {
        return followerName;
    }
    
    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }
    
    public String getFollowerAvatar() {
        return followerAvatar;
    }
    
    public void setFollowerAvatar(String followerAvatar) {
        this.followerAvatar = followerAvatar;
    }
    
    public String getFollowingName() {
        return followingName;
    }
    
    public void setFollowingName(String followingName) {
        this.followingName = followingName;
    }
    
    public String getFollowingAvatar() {
        return followingAvatar;
    }
    
    public void setFollowingAvatar(String followingAvatar) {
        this.followingAvatar = followingAvatar;
    }
    
    public Boolean getIsFollowingBack() {
        return isFollowingBack;
    }
    
    public void setIsFollowingBack(Boolean isFollowingBack) {
        this.isFollowingBack = isFollowingBack;
    }
}