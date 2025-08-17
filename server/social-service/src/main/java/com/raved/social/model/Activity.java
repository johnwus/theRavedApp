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
 * Activity Document for TheRavedApp MongoDB
 *
 * Represents a user activity in the social service. Converted from JPA entity
 * to MongoDB document.
 */
@Document(collection = "activities")
@CompoundIndexes({
    @CompoundIndex(name = "user_type_created_idx", def = "{'userId': 1, 'activityType': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "target_created_idx", def = "{'targetId': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "user_target_idx", def = "{'userId': 1, 'targetUserId': 1, 'createdAt': -1}")
})
public class Activity {
    
    @Id
    private String id;
    
    @Indexed
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @Indexed
    private String targetUserId;
    
    @Indexed
    private String targetId; // Generic target ID (post, comment, user, etc.)
    
    @Indexed
    private String targetType; // Type of target (POST, COMMENT, USER, etc.)
    
    @Indexed
    @NotBlank(message = "Activity type is required")
    private String activityType;
    
    @Indexed
    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;

    // Additional fields for backward compatibility with service layer
    @Indexed
    private String postId; // For post-related activities

    @Indexed
    private String commentId; // For comment-related activities

    // Additional MongoDB-specific fields for enhanced social features
    private LocalDateTime updatedAt;

    private String description; // Human-readable description

    private Map<String, Object> metadata; // Additional data

    private String visibility = "PUBLIC"; // PUBLIC, FRIENDS, PRIVATE

    @Indexed
    private Boolean isRead = false;

    private LocalDateTime readAt;

    private String source; // APP, WEB, API, etc.

    private String userAgent; // Browser/client information

    private String ipAddress; // IP address for analytics

    private String activityCategory; // SOCIAL, CONTENT, INTERACTION, etc.

    private Integer priority = 1; // Activity priority (1-5)

    private Boolean isAnonymous = false; // Anonymous activities

    private String location; // Geographic location if applicable

    private String deviceInfo; // Device information

    // Constructors
    public Activity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Activity(String userId, String activityType, String targetUserId, String targetId, String targetType) {
        this();
        this.userId = userId;
        this.activityType = activityType;
        this.targetUserId = targetUserId;
        this.targetId = targetId;
        this.targetType = targetType;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
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
    
    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(String activityCategory) {
        this.activityCategory = activityCategory;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    // Business logic methods
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsUnread() {
        this.isRead = false;
        this.readAt = null;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePriority(Integer newPriority) {
        if (newPriority >= 1 && newPriority <= 5) {
            this.priority = newPriority;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void setAnonymous(Boolean anonymous) {
        this.isAnonymous = anonymous;
        this.updatedAt = LocalDateTime.now();
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

    public boolean isHighPriority() {
        return this.priority != null && this.priority >= 4;
    }

    public boolean isPublic() {
        return "PUBLIC".equals(this.visibility);
    }

    public boolean isFriendsOnly() {
        return "FRIENDS".equals(this.visibility);
    }

    public boolean isPrivate() {
        return "PRIVATE".equals(this.visibility);
    }
    
    @Override
    public String toString() {
        return "Activity{" +
 "id='" + id + '\''
                + ", userId='" + userId + '\''
                + ", activityType='" + activityType + '\''
                + ", targetUserId='" + targetUserId + '\''
                + ", targetId='" + targetId + '\''
                + ", targetType='" + targetType + '\''
                + ", isRead=" + isRead
                +
                ", createdAt=" + createdAt +
                '}';
    }
}
