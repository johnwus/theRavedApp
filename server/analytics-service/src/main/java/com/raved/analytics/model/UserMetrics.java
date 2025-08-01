package com.raved.analytics.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * UserMetrics Entity for TheRavedApp
 *
 * Represents aggregated metrics for users.
 * Based on the user_metrics table schema.
 */
@Entity
@Table(name = "user_metrics", indexes = {
        @Index(name = "idx_user_metrics_user", columnList = "user_id"),
        @Index(name = "idx_user_metrics_date", columnList = "metric_date"),
        @Index(name = "idx_user_metrics_user_date", columnList = "user_id, metric_date"),
        @Index(name = "idx_user_metrics_engagement", columnList = "engagement_score"),
        @Index(name = "idx_user_metrics_activity", columnList = "activity_score")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_metrics_date", columnNames = { "user_id", "metric_date" })
})
public class UserMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user service

    @Column(name = "metric_date", nullable = false)
    private java.sql.Date metricDate;

    // Activity Metrics
    @Column(name = "posts_created", nullable = false)
    private Integer postsCreated = 0;

    @Column(name = "comments_made", nullable = false)
    private Integer commentsMade = 0;

    @Column(name = "likes_given", nullable = false)
    private Integer likesGiven = 0;

    @Column(name = "likes_received", nullable = false)
    private Integer likesReceived = 0;

    @Column(name = "shares_made", nullable = false)
    private Integer sharesMade = 0;

    @Column(name = "shares_received", nullable = false)
    private Integer sharesReceived = 0;

    @Column(name = "messages_sent", nullable = false)
    private Integer messagesSent = 0;

    @Column(name = "profile_views", nullable = false)
    private Integer profileViews = 0;

    // Engagement Metrics
    @Column(name = "session_count", nullable = false)
    private Integer sessionCount = 0;

    @Column(name = "total_session_duration", nullable = false)
    private Integer totalSessionDuration = 0; // in minutes

    @Column(name = "page_views", nullable = false)
    private Integer pageViews = 0;

    @Column(name = "unique_page_views", nullable = false)
    private Integer uniquePageViews = 0;

    // Ecommerce Metrics
    @Column(name = "products_viewed", nullable = false)
    private Integer productsViewed = 0;

    @Column(name = "products_liked", nullable = false)
    private Integer productsLiked = 0;

    @Column(name = "orders_placed", nullable = false)
    private Integer ordersPlaced = 0;

    @Column(name = "total_spent", precision = 10, scale = 2)
    private BigDecimal totalSpent = BigDecimal.ZERO;

    // Calculated Scores
    @Column(name = "engagement_score", precision = 5, scale = 2)
    private BigDecimal engagementScore = BigDecimal.ZERO;

    @Column(name = "activity_score", precision = 5, scale = 2)
    private BigDecimal activityScore = BigDecimal.ZERO;

    @Column(name = "influence_score", precision = 5, scale = 2)
    private BigDecimal influenceScore = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public UserMetrics() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UserMetrics(Long userId, java.sql.Date metricDate) {
        this();
        this.userId = userId;
        this.metricDate = metricDate;
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

    public java.sql.Date getMetricDate() {
        return metricDate;
    }

    public void setMetricDate(java.sql.Date metricDate) {
        this.metricDate = metricDate;
    }

    public Integer getPostsCreated() {
        return postsCreated;
    }

    public void setPostsCreated(Integer postsCreated) {
        this.postsCreated = postsCreated;
    }

    public Integer getCommentsMade() {
        return commentsMade;
    }

    public void setCommentsMade(Integer commentsMade) {
        this.commentsMade = commentsMade;
    }

    public Integer getLikesGiven() {
        return likesGiven;
    }

    public void setLikesGiven(Integer likesGiven) {
        this.likesGiven = likesGiven;
    }

    public Integer getLikesReceived() {
        return likesReceived;
    }

    public void setLikesReceived(Integer likesReceived) {
        this.likesReceived = likesReceived;
    }

    public Integer getSharesMade() {
        return sharesMade;
    }

    public void setSharesMade(Integer sharesMade) {
        this.sharesMade = sharesMade;
    }

    public Integer getSharesReceived() {
        return sharesReceived;
    }

    public void setSharesReceived(Integer sharesReceived) {
        this.sharesReceived = sharesReceived;
    }

    public Integer getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(Integer messagesSent) {
        this.messagesSent = messagesSent;
    }

    public Integer getProfileViews() {
        return profileViews;
    }

    public void setProfileViews(Integer profileViews) {
        this.profileViews = profileViews;
    }

    public Integer getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(Integer sessionCount) {
        this.sessionCount = sessionCount;
    }

    public Integer getTotalSessionDuration() {
        return totalSessionDuration;
    }

    public void setTotalSessionDuration(Integer totalSessionDuration) {
        this.totalSessionDuration = totalSessionDuration;
    }

    public Integer getPageViews() {
        return pageViews;
    }

    public void setPageViews(Integer pageViews) {
        this.pageViews = pageViews;
    }

    public Integer getUniquePageViews() {
        return uniquePageViews;
    }

    public void setUniquePageViews(Integer uniquePageViews) {
        this.uniquePageViews = uniquePageViews;
    }

    public Integer getProductsViewed() {
        return productsViewed;
    }

    public void setProductsViewed(Integer productsViewed) {
        this.productsViewed = productsViewed;
    }

    public Integer getProductsLiked() {
        return productsLiked;
    }

    public void setProductsLiked(Integer productsLiked) {
        this.productsLiked = productsLiked;
    }

    public Integer getOrdersPlaced() {
        return ordersPlaced;
    }

    public void setOrdersPlaced(Integer ordersPlaced) {
        this.ordersPlaced = ordersPlaced;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public BigDecimal getEngagementScore() {
        return engagementScore;
    }

    public void setEngagementScore(BigDecimal engagementScore) {
        this.engagementScore = engagementScore;
    }

    public BigDecimal getActivityScore() {
        return activityScore;
    }

    public void setActivityScore(BigDecimal activityScore) {
        this.activityScore = activityScore;
    }

    public BigDecimal getInfluenceScore() {
        return influenceScore;
    }

    public void setInfluenceScore(BigDecimal influenceScore) {
        this.influenceScore = influenceScore;
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
    public void incrementPostsCreated() {
        this.postsCreated++;
    }

    public void incrementCommentsMade() {
        this.commentsMade++;
    }

    public void incrementLikesGiven() {
        this.likesGiven++;
    }

    public void incrementLikesReceived() {
        this.likesReceived++;
    }

    public void incrementMessagesSent() {
        this.messagesSent++;
    }

    public void addSessionDuration(int minutes) {
        this.totalSessionDuration += minutes;
        this.sessionCount++;
    }

    public void addSpending(BigDecimal amount) {
        this.totalSpent = this.totalSpent.add(amount);
        this.ordersPlaced++;
    }

    public double getAverageSessionDuration() {
        return sessionCount > 0 ? (double) totalSessionDuration / sessionCount : 0.0;
    }

    public int getTotalEngagementActions() {
        return postsCreated + commentsMade + likesGiven + sharesMade + messagesSent;
    }

    public boolean isActiveUser() {
        return getTotalEngagementActions() > 0 || sessionCount > 0;
    }

    @Override
    public String toString() {
        return "UserMetrics{" +
                "id=" + id +
                ", userId=" + userId +
                ", metricDate=" + metricDate +
                ", postsCreated=" + postsCreated +
                ", sessionCount=" + sessionCount +
                ", engagementScore=" + engagementScore +
                ", activityScore=" + activityScore +
                '}';
    }
}
