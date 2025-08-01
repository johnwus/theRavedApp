package com.raved.content.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * PostMention Entity for TheRavedApp
 * 
 * Represents user mentions in posts.
 * Based on the post_mentions table schema.
 */
@Entity
@Table(name = "post_mentions", indexes = {
    @Index(name = "idx_post_mentions_post", columnList = "post_id"),
    @Index(name = "idx_post_mentions_user", columnList = "mentioned_user_id"),
    @Index(name = "idx_post_mentions_position", columnList = "post_id, start_position")
})
public class PostMention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "mentioned_user_id", nullable = false)
    private Long mentionedUserId; // Reference to user service

    @Column(name = "start_position", nullable = false)
    private Integer startPosition;

    @Column(name = "end_position", nullable = false)
    private Integer endPosition;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public PostMention() {
        this.createdAt = LocalDateTime.now();
    }

    public PostMention(Post post, Long mentionedUserId, Integer startPosition, Integer endPosition) {
        this();
        this.post = post;
        this.mentionedUserId = mentionedUserId;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Long getMentionedUserId() {
        return mentionedUserId;
    }

    public void setMentionedUserId(Long mentionedUserId) {
        this.mentionedUserId = mentionedUserId;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
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
        return "PostMention{" +
                "id=" + id +
                ", mentionedUserId=" + mentionedUserId +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", createdAt=" + createdAt +
                '}';
    }
}
