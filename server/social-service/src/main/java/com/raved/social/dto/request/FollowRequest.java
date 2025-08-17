package com.raved.social.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * FollowRequest for TheRavedApp
 * Updated for MongoDB compatibility with String
 * IDs
 */
public class FollowRequest {
    @NotBlank(message = "Follower ID is required")
    private String followerId;

    @NotBlank(message = "Following ID is required")
    private String followingId;
    
    // Default constructor
    public FollowRequest() {
    }
    
    // Constructor with all fields
    public FollowRequest(String followerId, String followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
    
    // Getters and Setters
    public String getFollowerId() {
        return followerId;
    }
    
    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }
    
    public String getFollowingId() {
        return followingId;
    }
    
    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }
}
