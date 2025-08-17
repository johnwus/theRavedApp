package com.raved.social.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Comment Document for TheRavedApp MongoDB
 *
 * Represents a comment on a post or another comment. Converted from JPA entity
 * to MongoDB document.
 */
@Document(collection = "comments")
@CompoundIndexes({
    @CompoundIndex(name = "post_created_idx", def = "{'postId': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "user_created_idx", def = "{'userId': 1, 'createdAt': -1}"),
    @CompoundIndex(name = "parent_created_idx", def = "{'parentCommentId': 1, 'createdAt': -1}")
})
public class Comment {

    @Id
    private String id;

    @Indexed
    @NotBlank(message = "Post ID is required")
    private String postId;

    @Indexed
    @NotBlank(message = "User ID is required")
    private String userId;

    @Indexed
    private String parentCommentId;

    @TextIndexed(weight = 5)
    @NotBlank(message = "Content is required")
    @Size(max = 2000, message = "Comment content must not exceed 2000 characters")
    private String content;

    @Indexed
    private Integer likesCount = 0;

    @Indexed
    private Integer repliesCount = 0;

    @Indexed
    private Boolean isFlagged = false;

    @Indexed
    private String moderationStatus = "APPROVED";

    @Indexed
    private Boolean isDeleted = false;

    @Indexed
    @NotNull(message = "Created at is required")
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime updatedAt;

    // Additional MongoDB-specific fields for enhanced social features
    private String authorName; // Cached author name for performance

    private String authorAvatar; // Cached author avatar URL

    private List<String> mentionedUserIds; // Users mentioned in the comment

    private List<String> hashtags; // Hashtags in the comment

    private String language = "en"; // Language of the comment

    private String sentiment = "NEUTRAL"; // AI-detected sentiment: POSITIVE, NEGATIVE, NEUTRAL

    private Map<String, Object> metadata; // For additional custom fields

    private String commentType = "TEXT"; // TEXT, RICH_TEXT, VOICE, VIDEO

    private Integer reportCount = 0; // Number of times reported

    private String lastModeratedBy; // Last moderator ID

    private LocalDateTime lastModeratedAt; // Last moderation timestamp

    private String moderationReason; // Reason for moderation action

    // Default constructor
    public Comment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with core fields
    public Comment(String postId, String userId, String content) {
        this();
        this.postId = postId;
        this.userId = userId;
        this.content = content;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(Integer repliesCount) {
        this.repliesCount = repliesCount;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public List<String> getMentionedUserIds() {
        return mentionedUserIds;
    }

    public void setMentionedUserIds(List<String> mentionedUserIds) {
        this.mentionedUserIds = mentionedUserIds;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public Integer getReportCount() {
        return reportCount;
    }

    public void setReportCount(Integer reportCount) {
        this.reportCount = reportCount;
    }

    public String getLastModeratedBy() {
        return lastModeratedBy;
    }

    public void setLastModeratedBy(String lastModeratedBy) {
        this.lastModeratedBy = lastModeratedBy;
    }

    public LocalDateTime getLastModeratedAt() {
        return lastModeratedAt;
    }

    public void setLastModeratedAt(LocalDateTime lastModeratedAt) {
        this.lastModeratedAt = lastModeratedAt;
    }

    public String getModerationReason() {
        return moderationReason;
    }

    public void setModerationReason(String moderationReason) {
        this.moderationReason = moderationReason;
    }


}