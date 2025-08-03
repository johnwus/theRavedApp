package com.raved.analytics.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for content metrics
 */
public class ContentMetricsResponse {

    private Long id;
    private Long contentId;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Long shareCount;
    private Double engagementScore;
    private Long reachCount;
    private Long impressionCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ContentMetricsResponse() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public Double getEngagementScore() {
        return engagementScore;
    }

    public void setEngagementScore(Double engagementScore) {
        this.engagementScore = engagementScore;
    }

    public Long getReachCount() {
        return reachCount;
    }

    public void setReachCount(Long reachCount) {
        this.reachCount = reachCount;
    }

    public Long getImpressionCount() {
        return impressionCount;
    }

    public void setImpressionCount(Long impressionCount) {
        this.impressionCount = impressionCount;
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
}
