package com.raved.content.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * PostTag Entity for TheRavedApp
 *
 * Represents tags associated with posts.
 * Based on the post_tags table schema.
 */
@Entity
@Table(name = "post_tags", indexes = {
        @Index(name = "idx_post_tags_post", columnList = "post_id"),
        @Index(name = "idx_post_tags_tag", columnList = "tag_name"),
        @Index(name = "idx_post_tags_trending", columnList = "tag_name, created_at")
})
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @NotBlank(message = "Tag name is required")
    @Size(max = 100, message = "Tag name must not exceed 100 characters")
    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public PostTag() {
        this.createdAt = LocalDateTime.now();
    }

    public PostTag(Post post, String tagName) {
        this();
        this.post = post;
        this.tagName = tagName;
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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
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
        return "PostTag{" +
                "id=" + id +
                ", tagName='" + tagName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
