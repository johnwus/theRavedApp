package com.raved.social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for user follow statistics
 */
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowStatsResponse {
    
    private Long userId;
    private String username;
    private Integer followersCount;
    private Integer followingCount;
    private Boolean isFollowing;
    private Boolean isFollowedBy;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public Integer getFollowersCount() { return followersCount; }
    public void setFollowersCount(Integer followersCount) { this.followersCount = followersCount; }
    
    public Integer getFollowingCount() { return followingCount; }
    public void setFollowingCount(Integer followingCount) { this.followingCount = followingCount; }
    
    public Boolean getIsFollowing() { return isFollowing; }
    public void setIsFollowing(Boolean isFollowing) { this.isFollowing = isFollowing; }
    
    public Boolean getIsFollowedBy() { return isFollowedBy; }
    public void setIsFollowedBy(Boolean isFollowedBy) { this.isFollowedBy = isFollowedBy; }
}
