package com.raved.analytics.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ContentMetrics Entity for TheRavedApp
 *
 * Represents aggregated metrics for content (posts, products, etc.).
 * Based on the content_metrics table schema.
 */
@Entity
@Table(name = "content_metrics", indexes = {
        @Index(name = "idx_content_metrics_entity", columnList = "entity_type, entity_id"),
        @Index(name = "idx_content_metrics_date", columnList = "metric_date"),
        @Index(name = "idx_content_metrics_views", columnList = "total_views"),
        @Index(name = "idx_content_metrics_engagement", columnList = "engagement_rate"),
        @Index(name = "idx_content_metrics_trending", columnList = "trending_score")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_content_metrics_date", columnNames = { "entity_type", "entity_id", "metric_date" })
})
public class ContentMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type", nullable = false)
    private String entityType; // "post", "product", "comment"

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "metric_date", nullable = false)
    private java.sql.Date metricDate;

    // View Metrics
    @Column(name = "total_views", nullable = false)
    private Integer totalViews = 0;

    @Column(name = "unique_views", nullable = false)
    private Integer uniqueViews = 0;

    @Column(name = "views_today", nullable = false)
    private Integer viewsToday = 0;

    // Engagement Metrics
    @Column(name = "total_likes", nullable = false)
    private Integer totalLikes = 0;

    @Column(name = "total_comments", nullable = false)
    private Integer totalComments = 0;

    @Column(name = "total_shares", nullable = false)
    private Integer totalShares = 0;

    @Column(name = "likes_today", nullable = false)
    private Integer likesToday = 0;

    @Column(name = "comments_today", nullable = false)
    private Integer commentsToday = 0;

    @Column(name = "shares_today", nullable = false)
    private Integer sharesToday = 0;

    // Calculated Metrics
    @Column(name = "engagement_rate", precision = 5, scale = 4)
    private BigDecimal engagementRate = BigDecimal.ZERO;

    @Column(name = "viral_coefficient", precision = 5, scale = 4)
    private BigDecimal viralCoefficient = BigDecimal.ZERO;

    @Column(name = "trending_score", precision = 8, scale = 2)
    private BigDecimal trendingScore = BigDecimal.ZERO;

    // Time-based Metrics
    @Column(name = "peak_hour")
    private Integer peakHour; // Hour of day with most activity (0-23)

    @Column(name = "average_session_duration")
    private Integer averageSessionDuration; // in seconds

    @Column(name = "bounce_rate", precision = 5, scale = 4)
    private BigDecimal bounceRate = BigDecimal.ZERO;

    // Geographic and Demographic
    @Column(name = "top_country")
    private String topCountry;

    @Column(name = "top_age_group")
    private String topAgeGroup;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public ContentMetrics() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ContentMetrics(String entityType, Long entityId, java.sql.Date metricDate) {
        this();
        this.entityType = entityType;
        this.entityId = entityId;
        this.metricDate = metricDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public java.sql.Date getMetricDate() {
        return metricDate;
    }

    public void setMetricDate(java.sql.Date metricDate) {
        this.metricDate = metricDate;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    public Integer getUniqueViews() {
        return uniqueViews;
    }

    public void setUniqueViews(Integer uniqueViews) {
        this.uniqueViews = uniqueViews;
    }

    public Integer getViewsToday() {
        return viewsToday;
    }

    public void setViewsToday(Integer viewsToday) {
        this.viewsToday = viewsToday;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    public Integer getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(Integer totalShares) {
        this.totalShares = totalShares;
    }

    public Integer getLikesToday() {
        return likesToday;
    }

    public void setLikesToday(Integer likesToday) {
        this.likesToday = likesToday;
    }

    public Integer getCommentsToday() {
        return commentsToday;
    }

    public void setCommentsToday(Integer commentsToday) {
        this.commentsToday = commentsToday;
    }

    public Integer getSharesToday() {
        return sharesToday;
    }

    public void setSharesToday(Integer sharesToday) {
        this.sharesToday = sharesToday;
    }

    public BigDecimal getEngagementRate() {
        return engagementRate;
    }

    public void setEngagementRate(BigDecimal engagementRate) {
        this.engagementRate = engagementRate;
    }

    public BigDecimal getViralCoefficient() {
        return viralCoefficient;
    }

    public void setViralCoefficient(BigDecimal viralCoefficient) {
        this.viralCoefficient = viralCoefficient;
    }

    public BigDecimal getTrendingScore() {
        return trendingScore;
    }

    public void setTrendingScore(BigDecimal trendingScore) {
        this.trendingScore = trendingScore;
    }

    public Integer getPeakHour() {
        return peakHour;
    }

    public void setPeakHour(Integer peakHour) {
        this.peakHour = peakHour;
    }

    public Integer getAverageSessionDuration() {
        return averageSessionDuration;
    }

    public void setAverageSessionDuration(Integer averageSessionDuration) {
        this.averageSessionDuration = averageSessionDuration;
    }

    public BigDecimal getBounceRate() {
        return bounceRate;
    }

    public void setBounceRate(BigDecimal bounceRate) {
        this.bounceRate = bounceRate;
    }

    public String getTopCountry() {
        return topCountry;
    }

    public void setTopCountry(String topCountry) {
        this.topCountry = topCountry;
    }

    public String getTopAgeGroup() {
        return topAgeGroup;
    }

    public void setTopAgeGroup(String topAgeGroup) {
        this.topAgeGroup = topAgeGroup;
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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Utility methods
    public void incrementViews() {
        this.totalViews++;
        this.viewsToday++;
    }

    public void incrementLikes() {
        this.totalLikes++;
        this.likesToday++;
    }

    public void incrementComments() {
        this.totalComments++;
        this.commentsToday++;
    }

    public void incrementShares() {
        this.totalShares++;
        this.sharesToday++;
    }

    public void calculateEngagementRate() {
        if (totalViews > 0) {
            int totalEngagements = totalLikes + totalComments + totalShares;
            this.engagementRate = BigDecimal.valueOf((double) totalEngagements / totalViews);
        }
    }

    public void calculateViralCoefficient() {
        if (totalViews > 0) {
            this.viralCoefficient = BigDecimal.valueOf((double) totalShares / totalViews);
        }
    }

    public int getTotalEngagements() {
        return totalLikes + totalComments + totalShares;
    }

    public int getTodayEngagements() {
        return likesToday + commentsToday + sharesToday;
    }

    public boolean isViral() {
        return viralCoefficient.compareTo(BigDecimal.valueOf(0.1)) > 0; // 10% viral threshold
    }

    public boolean isTrending() {
        return trendingScore.compareTo(BigDecimal.valueOf(100)) > 0; // Trending threshold
    }

    @Override
    public String toString() {
        return "ContentMetrics{" +
                "id=" + id +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", metricDate=" + metricDate +
                ", totalViews=" + totalViews +
                ", totalLikes=" + totalLikes +
                ", engagementRate=" + engagementRate +
                ", trendingScore=" + trendingScore +
                '}';
    }
}
