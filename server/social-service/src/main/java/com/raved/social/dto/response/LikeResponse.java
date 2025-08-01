package com.raved.social.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for Like entity
 */
public class LikeResponse {

    private Long id;
    private Long userId;
    private String username;
    private String userFullName;
    private String userProfilePictureUrl;
    private Long entityId;
    private String entityType; // "post" or "comment"
    private LocalDateTime createdAt;

    // Constructors
    public LikeResponse() {
    }

    public LikeResponse(Long userId, String username, Long entityId, String entityType) {
        this.userId = userId;
        this.username = username;
        this.entityId = entityId;
        this.entityType = entityType;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserProfilePictureUrl() {
        return userProfilePictureUrl;
    }

    public void setUserProfilePictureUrl(String userProfilePictureUrl) {
        this.userProfilePictureUrl = userProfilePictureUrl;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
