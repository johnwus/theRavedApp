package com.raved.social.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a user activity in the social service
 */
@Entity
@Table(name = "activities", indexes = {
    @Index(name = "idx_activities_user_id", columnList = "user_id"),
    @Index(name = "idx_activities_target_user_id", columnList = "target_user_id"),
    @Index(name = "idx_activities_post_id", columnList = "post_id"),
    @Index(name = "idx_activities_activity_type", columnList = "activity_type"),
    @Index(name = "idx_activities_created_at", columnList = "created_at")
})
public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "target_user_id")
    private Long targetUserId;
    
    @Column(name = "post_id")
    private Long postId;
    
    @Column(name = "comment_id")
    private Long commentId;
    
    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public Activity() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Activity(Long userId, String activityType, Long targetUserId, Long postId, Long commentId) {
        this();
        this.userId = userId;
        this.activityType = activityType;
        this.targetUserId = targetUserId;
        this.postId = postId;
        this.commentId = commentId;
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
    
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    
    public Long getTargetUserId() {
        return targetUserId;
    }
    
    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }
    
    public Long getPostId() {
        return postId;
    }
    
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public Long getCommentId() {
        return commentId;
    }
    
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", userId=" + userId +
                ", activityType=" + activityType +
                ", targetUserId=" + targetUserId +
                ", postId=" + postId +
                ", commentId=" + commentId +
                ", createdAt=" + createdAt +
                '}';
    }
}
