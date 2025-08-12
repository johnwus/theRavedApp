package com.raved.content.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Post Document for TheRavedApp MongoDB
 *
 * Represents posts in the content service using MongoDB.
 * Converted from JPA entity to MongoDB document.
 */
@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    @Indexed
    private String userId; // Reference to user service

    @NotBlank(message = "Content is required")
    @TextIndexed(weight = 2)
    private String content;

    // Types and visibility
    @Indexed
    private String postType = "OUTFIT"; // OUTFIT, GENERAL, POLL, EVENT

    @Indexed
    private String visibility = "PUBLIC"; // PUBLIC, FACULTY_ONLY, FOLLOWERS_ONLY, CONNECTIONS_ONLY, PRIVATE

    @Indexed
    private String facultyId; // Reference to user service

    // Drafts & scheduling
    @Indexed
    private String publishStatus = "PUBLISHED"; // DRAFT, SCHEDULED, PUBLISHED, ARCHIVED

    private LocalDateTime scheduledAt;

    // Denormalized metrics
    @Indexed
    private Integer likesCount = 0;

    @Indexed
    private Integer commentsCount = 0;

    @Indexed
    private Integer sharesCount = 0;

    @Indexed
    private Integer viewsCount = 0;

    @Indexed
    private Integer savesCount = 0;

    // Moderation
    @Indexed
    private Boolean isFlagged = false;

    @Indexed
    private String moderationStatus = "APPROVED";

    private String flaggedReason;

    // System flags
    @Indexed
    private Boolean isDeleted = false;

    @Indexed
    private Boolean isFeatured = false;

    private LocalDateTime featuredAt;

    private LocalDateTime featuredUntil;

    @Indexed
    private Boolean isPinned = false;

    private LocalDateTime pinnedAt;

    @Indexed
    private Boolean isEdited = false;

    private LocalDateTime editedAt;

    @Indexed
    private Boolean allowComments = true;

    @Indexed
    private Boolean allowSharing = true;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime updatedAt;

    // Additional MongoDB-specific fields for better content management
    private String title;

    @TextIndexed(weight = 3)
    private List<String> tags;

    @Indexed
    private String category;

    @Indexed
    private String language = "en";

    @Indexed
    private String accessLevel = "PUBLIC";

    @Indexed
    private Double engagementScore = 0.0;

    @Indexed
    private Double trendingScore = 0.0;

    @Indexed
    private Double viralityScore = 0.0;

    @Indexed
    private String sentiment = "NEUTRAL";

    private List<String> contentFlags;

    private String seoTitle;
    private String seoDescription;
    private List<String> seoKeywords;

    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;

    private String moderatorId;
    private String moderationReason;
    private LocalDateTime moderatedAt;

    private String flagReason;
    private LocalDateTime flaggedAt;

    private Integer version = 1;
    private List<Map<String, Object>> changeHistory;

    private Map<String, Object> metadata;
    private Map<String, Object> analytics;
    private Map<String, Object> customFields;

    // Relationships - embedded or referenced
    private List<String> mediaFileIds; // Reference to media files
    private List<String> commentIds; // Reference to comments
    private List<String> relatedPostIds; // Reference to related posts
    private Map<String, String> crossReferences; // Reference to other content

    // Constructors
    public Post() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post(String userId, String content, String postType, String visibility) {
        this();
        this.userId = userId;
        this.content = content;
        this.postType = postType;
        this.visibility = visibility;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Integer getSharesCount() {
        return sharesCount;
    }

    public void setSharesCount(Integer sharesCount) {
        this.sharesCount = sharesCount;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Integer getSavesCount() {
        return savesCount;
    }

    public void setSavesCount(Integer savesCount) {
        this.savesCount = savesCount;
    }

    public Boolean getIsFlagged() {
        return isFlagged;
    }

    public void setIsFlagged(Boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public String getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(String moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public String getFlaggedReason() {
        return flaggedReason;
    }

    public void setFlaggedReason(String flaggedReason) {
        this.flaggedReason = flaggedReason;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public LocalDateTime getFeaturedUntil() {
        return featuredUntil;
    }

    public void setFeaturedUntil(LocalDateTime featuredUntil) {
        this.featuredUntil = featuredUntil;
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Double getEngagementScore() {
        return engagementScore;
    }

    public void setEngagementScore(Double engagementScore) {
        this.engagementScore = engagementScore;
    }

    public Double getTrendingScore() {
        return trendingScore;
    }

    public void setTrendingScore(Double trendingScore) {
        this.trendingScore = trendingScore;
    }

    public Double getViralityScore() {
        return viralityScore;
    }

    public void setViralityScore(Double viralityScore) {
        this.viralityScore = viralityScore;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public List<String> getContentFlags() {
        return contentFlags;
    }

    public void setContentFlags(List<String> contentFlags) {
        this.contentFlags = contentFlags;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public List<String> getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(List<String> seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(String moderatorId) {
        this.moderatorId = moderatorId;
    }

    public String getModerationReason() {
        return moderationReason;
    }

    public void setModerationReason(String moderationReason) {
        this.moderationReason = moderationReason;
    }

    public LocalDateTime getModeratedAt() {
        return moderatedAt;
    }

    public void setModeratedAt(LocalDateTime moderatedAt) {
        this.moderatedAt = moderatedAt;
    }

    public Boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }

    public LocalDateTime getFlaggedAt() {
        return flaggedAt;
    }

    public void setFlaggedAt(LocalDateTime flaggedAt) {
        this.flaggedAt = flaggedAt;
    }

    public List<String> getMediaFileIds() {
        return mediaFileIds;
    }

    public void setMediaFileIds(List<String> mediaFileIds) {
        this.mediaFileIds = mediaFileIds;
    }

    public void setCrossReferences(Map<String, String> crossReferences) {
        this.crossReferences = crossReferences;
    }

    // =============================================================================
    // BUSINESS LOGIC METHODS
    // =============================================================================

    /**
     * Increment likes count
     */
    public void incrementLikes() {
        this.likesCount = (this.likesCount != null ? this.likesCount : 0) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Decrement likes count
     */
    public void decrementLikes() {
        if (this.likesCount != null && this.likesCount > 0) {
            this.likesCount--;
            this.updatedAt = LocalDateTime.now();
        }
    }

    /**
     * Increment comments count
     */
    public void incrementComments() {
        this.commentsCount = (this.commentsCount != null ? this.commentsCount : 0) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Decrement comments count
     */
    public void decrementComments() {
        if (this.commentsCount != null && this.commentsCount > 0) {
            this.commentsCount--;
            this.updatedAt = LocalDateTime.now();
        }
    }

    /**
     * Increment shares count
     */
    public void incrementShares() {
        this.sharesCount = (this.sharesCount != null ? this.sharesCount : 0) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Decrement shares count
     */
    public void decrementShares() {
        if (this.sharesCount != null && this.sharesCount > 0) {
            this.sharesCount--;
            this.updatedAt = LocalDateTime.now();
        }
    }

    /**
     * Increment views count
     */
    public void incrementViews() {
        this.viewsCount = (this.viewsCount != null ? this.viewsCount : 0) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Increment saves count
     */
    public void incrementSaves() {
        this.savesCount = (this.savesCount != null ? this.savesCount : 0) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Decrement saves count
     */
    public void decrementSaves() {
        if (this.savesCount != null && this.savesCount > 0) {
            this.savesCount--;
            this.updatedAt = LocalDateTime.now();
        }
    }

    /**
     * Mark post as featured
     */
    public void markAsFeatured() {
        this.isFeatured = true;
        this.featuredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Unmark post as featured
     */
    public void unmarkAsFeatured() {
        this.isFeatured = false;
        this.featuredAt = null;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Mark post as pinned
     */
    public void markAsPinned() {
        this.isPinned = true;
        this.pinnedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Unmark post as pinned
     */
    public void unmarkAsPinned() {
        this.isPinned = false;
        this.pinnedAt = null;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Mark post as edited
     */
    public void markAsEdited() {
        this.isEdited = true;
        this.editedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Update engagement score
     */
    public void updateEngagementScore(double score) {
        this.engagementScore = score;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Update trending score
     */
    public void updateTrendingScore(double score) {
        this.trendingScore = score;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Update virality score
     */
    public void updateViralityScore(double score) {
        this.viralityScore = score;
        this.updatedAt = LocalDateTime.now();
    }

    // =============================================================================
    // ADDITIONAL GETTERS AND SETTERS FOR MISSING METHODS
    // =============================================================================

    public Boolean getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    public Boolean getAllowComments() {
        return allowComments;
    }

    public void setAllowComments(Boolean allowComments) {
        this.allowComments = allowComments;
    }

    public Boolean getAllowSharing() {
        return allowSharing;
    }

    public void setAllowSharing(Boolean allowSharing) {
        this.allowSharing = allowSharing;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    public LocalDateTime getFeaturedAt() {
        return featuredAt;
    }

    public void setFeaturedAt(LocalDateTime featuredAt) {
        this.featuredAt = featuredAt;
    }

    public LocalDateTime getPinnedAt() {
        return pinnedAt;
    }

    public void setPinnedAt(LocalDateTime pinnedAt) {
        this.pinnedAt = pinnedAt;
    }

    public String getFlagReason() {
        return flagReason;
    }

    public void setFlagReason(String flagReason) {
        this.flagReason = flagReason;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Map<String, Object>> getChangeHistory() {
        return changeHistory;
    }

    public void setChangeHistory(List<Map<String, Object>> changeHistory) {
        this.changeHistory = changeHistory;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getAnalytics() {
        return analytics;
    }

    public void setAnalytics(Map<String, Object> analytics) {
        this.analytics = analytics;
    }

    public Map<String, Object> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, Object> customFields) {
        this.customFields = customFields;
    }

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    public List<String> getRelatedPostIds() {
        return relatedPostIds;
    }

    public void setRelatedPostIds(List<String> relatedPostIds) {
        this.relatedPostIds = relatedPostIds;
    }

    public Map<String, String> getCrossReferences() {
        return crossReferences;
    }

    // =============================================================================
    // LEGACY METHOD ALIASES FOR COMPATIBILITY
    // =============================================================================

    public Integer getLikeCount() {
        return likesCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likesCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentsCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentsCount = commentCount;
    }

    public Integer getShareCount() {
        return sharesCount;
    }

    public void setShareCount(Integer shareCount) {
        this.sharesCount = shareCount;
    }

    public Integer getViewCount() {
        return viewsCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewsCount = viewCount;
    }

    public Integer getSaveCount() {
        return savesCount;
    }

    public void setSaveCount(Integer saveCount) {
        this.savesCount = saveCount;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", postType='" + postType + '\'' +
                ", visibility='" + visibility + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
