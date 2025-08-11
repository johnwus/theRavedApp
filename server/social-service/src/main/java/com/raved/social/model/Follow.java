package com.raved.social.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a follow relationship between users
 */
@Entity
@Table(name = "follows", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}),
       indexes = {
           @Index(name = "idx_follows_follower_id", columnList = "follower_id"),
           @Index(name = "idx_follows_following_id", columnList = "following_id"),
           @Index(name = "idx_follows_created_at", columnList = "created_at")
       })
public class Follow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "follower_id", nullable = false)
    private Long followerId;
    
    @Column(name = "following_id", nullable = false)
    private Long followingId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Default constructor
    public Follow() {
    }
    
    // Constructor with all fields
    public Follow(Long id, Long followerId, Long followingId, LocalDateTime createdAt) {
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFollowerId() {
        return followerId;
    }
    
    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }
    
    public Long getFollowingId() {
        return followingId;
    }
    
    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
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
}