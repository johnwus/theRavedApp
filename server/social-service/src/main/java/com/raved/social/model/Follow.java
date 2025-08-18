package com.raved.social.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Follow Document for TheRavedApp MongoDB
 *
 * Represents a follow relationship between users. Converted from JPA entity to
 * MongoDB document.
 */
@Document(collection = "follows")
@CompoundIndexes({
    @CompoundIndex(name = "follower_following_idx", def = "{'followerId': 1, 'followingId': 1}", unique = true),
    @CompoundIndex(name = "follower_created_idx", def = "{'followerId': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "following_created_idx", def = "{'followingId': 1, 'createdAt': -1}")
})
public class Follow {
    
    @Id
    private String id;
    
    @Indexed
    @NotBlank(message = "Follower ID is required")
    private String followerId;
    
    @Indexed
    @NotBlank(message = "Following ID is required")
    private String followingId;
    
    @Indexed
    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;
    
    // Additional MongoDB-specific fields for enhanced social features
    private LocalDateTime updatedAt;

    @Indexed
    private String status = "ACTIVE"; // ACTIVE, BLOCKED, MUTED

    private String notificationPreference = "ALL"; // ALL, IMPORTANT, NONE

    private Map<String, Object> metadata; // For additional custom fields

    private String followSource; // APP, SUGGESTION, SEARCH, etc.

    private Boolean isMutual = false; // If both users follow each other

    private LocalDateTime lastInteractionAt; // Last time they interacted

    private String relationshipStrength = "WEAK"; // WEAK, MEDIUM, STRONG based on interactions

    // Default constructor
    public Follow() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor with core fields
    public Follow(String followerId, String followingId) {
        this();
        this.followerId = followerId;
        this.followingId = followingId;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotificationPreference() {
        return notificationPreference;
    }

    public void setNotificationPreference(String notificationPreference) {
        this.notificationPreference = notificationPreference;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getFollowSource() {
        return followSource;
    }

    public void setFollowSource(String followSource) {
        this.followSource = followSource;
    }

    public Boolean getIsMutual() {
        return isMutual;
    }

    public void setIsMutual(Boolean isMutual) {
        this.isMutual = isMutual;
    }

    public LocalDateTime getLastInteractionAt() {
        return lastInteractionAt;
    }

    public void setLastInteractionAt(LocalDateTime lastInteractionAt) {
        this.lastInteractionAt = lastInteractionAt;
    }

    public String getRelationshipStrength() {
        return relationshipStrength;
    }

    public void setRelationshipStrength(String relationshipStrength) {
        this.relationshipStrength = relationshipStrength;
    }

    // Business logic methods
    public void updateLastInteraction() {
        this.lastInteractionAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsMutual() {
        this.isMutual = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void block() {
        this.status = "BLOCKED";
        this.updatedAt = LocalDateTime.now();
    }

    public void mute() {
        this.status = "MUTED";
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = "ACTIVE";
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Follow{"
                + "id='" + id + '\''
                + ", followerId='" + followerId + '\''
                + ", followingId='" + followingId + '\''
                + ", status='" + status + '\''
                + ", createdAt=" + createdAt
                + '}';
    }
}