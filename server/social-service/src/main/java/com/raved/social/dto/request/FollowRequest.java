package com.raved.social.dto.request;

/**
 * FollowRequest for TheRavedApp
 */
public class FollowRequest {
    private Long followerId;
    private Long followingId;
    
    // Default constructor
    public FollowRequest() {
    }
    
    // Constructor with all fields
    public FollowRequest(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
    
    // Getters and Setters
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
}
