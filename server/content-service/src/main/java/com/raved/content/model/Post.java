package com.raved.content.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Post Entity for TheRavedApp
 *
 * Represents posts in the content service.
 * Based on the posts table schema.
 */
@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_posts_user_id", columnList = "user_id"),
        @Index(name = "idx_posts_faculty_id", columnList = "faculty_id"),
        @Index(name = "idx_posts_created_at", columnList = "created_at"),
        @Index(name = "idx_posts_featured", columnList = "is_featured, featured_until"),
        @Index(name = "idx_posts_visibility", columnList = "visibility")
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user service

    @NotBlank(message = "Content is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // Types and visibility
    @Column(name = "post_type", nullable = false, length = 20)
    private String postType = "OUTFIT"; // OUTFIT, GENERAL, POLL, EVENT

    @Column(nullable = false, length = 20)
    private String visibility = "PUBLIC"; // PUBLIC, FACULTY_ONLY, FOLLOWERS_ONLY, CONNECTIONS_ONLY, PRIVATE

    @Column(name = "faculty_id")
    private Long facultyId; // Reference to user service

    // Drafts & scheduling
    @Column(name = "publish_status", nullable = false, length = 12)
    private String publishStatus = "PUBLISHED"; // DRAFT, SCHEDULED, PUBLISHED, ARCHIVED

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    // Denormalized metrics
    @Column(name = "likes_count", nullable = false)
    private Integer likesCount = 0;

    @Column(name = "comments_count", nullable = false)
    private Integer commentsCount = 0;

    @Column(name = "shares_count", nullable = false)
    private Integer sharesCount = 0;

    @Column(name = "views_count", nullable = false)
    private Integer viewsCount = 0;

    @Column(name = "saves_count", nullable = false)
    private Integer savesCount = 0;

    // Moderation
    @Column(name = "is_flagged", nullable = false)
    private Boolean isFlagged = false;

    @Column(name = "moderation_status", nullable = false, length = 20)
    private String moderationStatus = "APPROVED";

    @Column(name = "flagged_reason", columnDefinition = "TEXT")
    private String flaggedReason;

    // System flags
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @Column(name = "featured_until")
    private LocalDateTime featuredUntil;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MediaFile> mediaFiles;

    // Constructors
    public Post() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post(Long userId, String content, String postType, String visibility) {
        this();
        this.userId = userId;
        this.content = content;
        this.postType = postType;
        this.visibility = visibility;
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

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
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

    public List<MediaFile> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<MediaFile> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    // Lifecycle methods
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business logic methods
    public void incrementLikes() {
        if (this.likesCount == null) {
            this.likesCount = 0;
        }
        this.likesCount++;
    }

    public void decrementLikes() {
        if (this.likesCount != null && this.likesCount > 0) {
            this.likesCount--;
        }
    }

    public void incrementComments() {
        if (this.commentsCount == null) {
            this.commentsCount = 0;
        }
        this.commentsCount++;
    }

    public void decrementComments() {
        if (this.commentsCount != null && this.commentsCount > 0) {
            this.commentsCount--;
        }
    }

    public void incrementShares() {
        if (this.sharesCount == null) {
            this.sharesCount = 0;
        }
        this.sharesCount++;
    }

    public void incrementViews() {
        if (this.viewsCount == null) {
            this.viewsCount = 0;
        }
        this.viewsCount++;
    }

    public void incrementSaves() {
        if (this.savesCount == null) {
            this.savesCount = 0;
        }
        this.savesCount++;
    }

    public void decrementSaves() {
        if (this.savesCount != null && this.savesCount > 0) {
            this.savesCount--;
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", postType='" + postType + '\'' +
                ", visibility='" + visibility + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
