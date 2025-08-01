package com.raved.social.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Like Entity for TheRavedApp
 *
 * Represents likes on posts and comments.
 * Based on the likes table schema.
 */
@Entity
@Table(name = "likes", indexes = {
        @Index(name = "idx_likes_post", columnList = "post_id"),
        @Index(name = "idx_likes_comment", columnList = "comment_id"),
        @Index(name = "idx_likes_user", columnList = "user_id"),
        @Index(name = "idx_likes_user_post", columnList = "user_id, post_id"),
        @Index(name = "idx_likes_user_comment", columnList = "user_id, comment_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_like_user_post", columnNames = { "user_id", "post_id" }),
        @UniqueConstraint(name = "uk_like_user_comment", columnNames = { "user_id", "comment_id" })
})
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user service

    @Column(name = "post_id")
    private Long postId; // Reference to content service (nullable for comment likes)

    @Column(name = "comment_id")
    private Long commentId; // Reference to comment (nullable for post likes)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Like() {
        this.createdAt = LocalDateTime.now();
    }

    public Like(Long userId, Long postId) {
        this();
        this.userId = userId;
        this.postId = postId;
    }

    public Like(Long userId, Long postId, Long commentId) {
        this();
        this.userId = userId;
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

    // Utility methods
    public boolean isPostLike() {
        return postId != null && commentId == null;
    }

    public boolean isCommentLike() {
        return commentId != null;
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", userId=" + userId +
                ", postId=" + postId +
                ", commentId=" + commentId +
                ", createdAt=" + createdAt +
                '}';
    }
}
