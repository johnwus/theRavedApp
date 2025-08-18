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
 * Like Document for TheRavedApp MongoDB
 *
 * Represents a like on a post, comment, or other content. Converted from JPA
 * entity to MongoDB document.
 */
@Document(collection = "likes")
@CompoundIndexes({
    @CompoundIndex(name = "content_user_idx", def = "{'contentId': 1, 'userId': 1}", unique = true),
    @CompoundIndex(name = "user_created_idx", def = "{'userId': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "content_created_idx", def = "{'contentId': 1, 'createdAt': -1}")
})
public class Like {

    /**
     * Target type enum for backward compatibility with service layer
     */
    public enum TargetType {
        POST, COMMENT, MEDIA, STORY, REEL, LIVE_STREAM, POLL, EVENT
    }

    @Id
    private String id;

    @Indexed
    @NotBlank(message = "Content ID is required")
    private String contentId;

    @Indexed
    @NotBlank(message = "User ID is required")
    private String userId;

    @Indexed
    @NotBlank(message = "Content type is required")
    private String contentType; // POST, COMMENT, MEDIA, etc.

    // Additional fields for compatibility with service layer
    @Indexed
    private String targetId; // Same as contentId for backward compatibility

    @Indexed
    private TargetType targetType; // Enum version of contentType

    @Indexed
    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;

    // Additional MongoDB-specific fields for enhanced social features
    private LocalDateTime updatedAt;

    @Indexed
    private String likeType = "LIKE"; // LIKE, LOVE, HAHA, WOW, SAD, ANGRY

    private String reactionEmoji; // Custom emoji if applicable

    private Boolean isAnonymous = false; // Anonymous likes

    private String likeSource; // APP, WEB, API, etc.

    private Map<String, Object> metadata; // For additional custom fields

    private String userAgent; // Browser/client information

    private String ipAddress; // IP address for analytics

    private Boolean isRemoved = false; // Soft delete

    private LocalDateTime removedAt; // When the like was removed

    private String removalReason; // Why it was removed

    // Default constructor
    public Like() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with core fields
    public Like(String contentId, String userId, String contentType) {
        this();
        this.contentId = contentId;
        this.userId = userId;
        this.contentType = contentType;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public String getLikeType() {
        return likeType;
    }

    public void setLikeType(String likeType) {
        this.likeType = likeType;
    }

    public String getReactionEmoji() {
        return reactionEmoji;
    }

    public void setReactionEmoji(String reactionEmoji) {
        this.reactionEmoji = reactionEmoji;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getLikeSource() {
        return likeSource;
    }

    public void setLikeSource(String likeSource) {
        this.likeSource = likeSource;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getIsRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(Boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

    public LocalDateTime getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDateTime removedAt) {
        this.removedAt = removedAt;
    }

    public String getRemovalReason() {
        return removalReason;
    }

    public void setRemovalReason(String removalReason) {
        this.removalReason = removalReason;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    // Business logic methods
    public void remove(String reason) {
        this.isRemoved = true;
        this.removedAt = LocalDateTime.now();
        this.removalReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void restore() {
        this.isRemoved = false;
        this.removedAt = null;
        this.removalReason = null;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeReaction(String newLikeType, String emoji) {
        this.likeType = newLikeType;
        this.reactionEmoji = emoji;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return !this.isRemoved;
    }

    @Override
    public String toString() {
        return "Like{"
                + "id='" + id + '\''
                + ", contentId='" + contentId + '\''
                + ", userId='" + userId + '\''
                + ", contentType='" + contentType + '\''
                + ", likeType='" + likeType + '\''
                + ", isRemoved=" + isRemoved
                + ", createdAt=" + createdAt
                + '}';
    }
}