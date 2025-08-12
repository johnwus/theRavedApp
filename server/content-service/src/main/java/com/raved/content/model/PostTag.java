package com.raved.content.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * PostTag Document for TheRavedApp MongoDB
 *
 * Represents tags associated with posts.
 * Converted from JPA entity to MongoDB document.
 */
@Document(collection = "post_tags")
public class PostTag {

    @Id
    private String id;

    @Indexed
    private String postId; // Reference to post instead of embedded object

    @NotBlank(message = "Tag name is required")
    @Size(max = 100, message = "Tag name must not exceed 100 characters")
    @TextIndexed(weight = 3)
    private String tagName;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime updatedAt;

    // Additional MongoDB-specific fields for better tag management
    private String description;

    private String color;

    private String icon;

    @Indexed
    private String category;

    @Indexed
    private Integer usageCount = 0;

    @Indexed
    private String status = "ACTIVE";

    private List<String> relatedTags;

    private String createdBy;

    private String moderatedBy;

    private String moderatorId;

    private LocalDateTime moderatedAt;

    private String moderationReason;

    // Tag analytics
    @Indexed
    private Integer viewCount = 0;

    @Indexed
    private Integer clickCount = 0;

    @Indexed
    private Integer searchCount = 0;

    // Tag relationships
    private List<String> parentTags;
    private List<String> childTags;

    // Tag metadata
    private String language = "en";
    private String region;
    private Map<String, Object> metadata;

    // Constructors
    public PostTag() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public PostTag(String postId, String tagName) {
        this();
        this.postId = postId;
        this.tagName = tagName;
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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
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

    // New MongoDB-specific getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getRelatedTags() {
        return relatedTags;
    }

    public void setRelatedTags(List<String> relatedTags) {
        this.relatedTags = relatedTags;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModeratedBy() {
        return moderatedBy;
    }

    public void setModeratedBy(String moderatedBy) {
        this.moderatedBy = moderatedBy;
    }

    public String getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(String moderatorId) {
        this.moderatorId = moderatorId;
    }

    public LocalDateTime getModeratedAt() {
        return moderatedAt;
    }

    public void setModeratedAt(LocalDateTime moderatedAt) {
        this.moderatedAt = moderatedAt;
    }

    public String getModerationReason() {
        return moderationReason;
    }

    public void setModerationReason(String moderationReason) {
        this.moderationReason = moderationReason;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Integer searchCount) {
        this.searchCount = searchCount;
    }

    public List<String> getParentTags() {
        return parentTags;
    }

    public void setParentTags(List<String> parentTags) {
        this.parentTags = parentTags;
    }

    public List<String> getChildTags() {
        return childTags;
    }

    public void setChildTags(List<String> childTags) {
        this.childTags = childTags;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    // Business logic methods
    public void incrementUsageCount() {
        if (this.usageCount == null) {
            this.usageCount = 0;
        }
        this.usageCount++;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementViewCount() {
        if (this.viewCount == null) {
            this.viewCount = 0;
        }
        this.viewCount++;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementClickCount() {
        if (this.clickCount == null) {
            this.clickCount = 0;
        }
        this.clickCount++;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementSearchCount() {
        if (this.searchCount == null) {
            this.searchCount = 0;
        }
        this.searchCount++;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "PostTag{" +
                "id='" + id + '\'' +
                ", tagName='" + tagName + '\'' +
                ", category='" + category + '\'' +
                ", usageCount=" + usageCount +
                ", status='" + status + '\'' +
                '}';
    }
}
