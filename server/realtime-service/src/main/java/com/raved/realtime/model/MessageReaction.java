package com.raved.realtime.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a reaction to a message
 */
@Entity
@Table(name = "message_reactions", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"message_id", "user_id", "reaction_type"}),
       indexes = {
           @Index(name = "idx_message_reactions_message", columnList = "message_id"),
           @Index(name = "idx_message_reactions_user", columnList = "user_id")
       })
public class MessageReaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "message_id", nullable = false)
    private Long messageId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "reaction_type", nullable = false, length = 20)
    private String reactionType; // LIKE, LOVE, LAUGH, etc.
    
    @Column(name = "emoji", length = 10)
    private String emoji; // Unicode emoji character
    
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructors
    public MessageReaction() {
        this.createdAt = LocalDateTime.now();
    }

    public MessageReaction(Long messageId, Long userId, String reactionType) {
        this();
        this.messageId = messageId;
        this.userId = userId;
        this.reactionType = reactionType;
    }

    public MessageReaction(Long messageId, Long userId, String reactionType, String emoji) {
        this();
        this.messageId = messageId;
        this.userId = userId;
        this.reactionType = reactionType;
        this.emoji = emoji;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "MessageReaction{" +
                "id=" + id +
                ", messageId=" + messageId +
                ", userId=" + userId +
                ", reactionType='" + reactionType + '\'' +
                ", emoji='" + emoji + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}