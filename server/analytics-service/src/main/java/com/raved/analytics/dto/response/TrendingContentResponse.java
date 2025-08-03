package com.raved.analytics.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for trending content
 */
public class TrendingContentResponse {

    private Long contentId;
    private String title;
    private String category;
    private Long authorId;
    private String authorName;
    private Double trendingScore;
    private Integer trendingRank;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Long shareCount;
    private LocalDateTime createdAt;
    private LocalDateTime trendingSince;

    // Constructors
    public TrendingContentResponse() {}

    public TrendingContentResponse(Long contentId, Double trendingScore, Integer trendingRank) {
        this.contentId = contentId;
        this.trendingScore = trendingScore;
        this.trendingRank = trendingRank;
    }

    // Getters and Setters
    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Double getTrendingScore() {
        return trendingScore;
    }

    public void setTrendingScore(Double trendingScore) {
        this.trendingScore = trendingScore;
    }

    public Integer getTrendingRank() {
        return trendingRank;
    }

    public void setTrendingRank(Integer trendingRank) {
        this.trendingRank = trendingRank;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getTrendingSince() {
        return trendingSince;
    }

    public void setTrendingSince(LocalDateTime trendingSince) {
        this.trendingSince = trendingSince;
    }
}
