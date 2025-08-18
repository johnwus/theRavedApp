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
 * SavedPost Document for TheRavedApp MongoDB
 *
 * Represents a saved post (bookmark) by a user. Converted from JPA entity to
 * MongoDB document.
 */
@Document(collection = "saved_posts")
@CompoundIndexes({
    @CompoundIndex(name = "user_post_idx", def = "{'userId': 1, 'postId': 1}", unique = true),
    @CompoundIndex(name = "user_created_idx", def = "{'userId': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "post_created_idx", def = "{'postId': 1, 'createdAt': -1}")
})
public class SavedPost {

    @Id
    private String id;

    @Indexed
    @NotBlank(message = "User ID is required")
    private String userId;

    @Indexed
    @NotBlank(message = "Post ID is required")
    private String postId;

    @Indexed
    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;

    // Additional MongoDB-specific fields for enhanced social features
    private LocalDateTime updatedAt;

    private String collectionName; // Custom collection name for organization

    private String note; // User's personal note about the post

    private Map<String, Object> metadata; // For additional custom fields

    private String saveSource; // APP, WEB, SHARE, etc.

    @Indexed
    private Boolean isPublic = false; // Whether the save is visible to others

    private String category; // User-defined category for the saved post

    private String tags; // User-defined tags

    private Integer priority = 1; // User-defined priority (1-5)

    private String reminderDate; // Optional reminder date

    private Boolean isArchived = false; // Archive status

    private LocalDateTime archivedAt; // When it was archived

    // Default constructor
    public SavedPost() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with core fields
    public SavedPost(String userId, String postId) {
        this();
        this.userId = userId;
        this.postId = postId;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getSaveSource() {
        return saveSource;
    }

    public void setSaveSource(String saveSource) {
        this.saveSource = saveSource;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    // Business logic methods
    public void archive() {
        this.isArchived = true;
        this.archivedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void unarchive() {
        this.isArchived = false;
        this.archivedAt = null;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePriority(Integer newPriority) {
        if (newPriority >= 1 && newPriority <= 5) {
            this.priority = newPriority;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void setPublic(Boolean isPublic) {
        this.isPublic = isPublic;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isHighPriority() {
        return this.priority != null && this.priority >= 4;
    }

    @Override
    public String toString() {
        return "SavedPost{"
                + "id='" + id + '\''
                + ", userId='" + userId + '\''
                + ", postId='" + postId + '\''
                + ", collectionName='" + collectionName + '\''
                + ", isPublic=" + isPublic
                + ", isArchived=" + isArchived
                + ", createdAt=" + createdAt
                + '}';
    }
}
