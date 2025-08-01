package com.raved.realtime.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * MessageReaction Entity for TheRavedApp
 * 
 * Represents reactions to messages (emojis, likes, etc.).
 * Based on the message_reactions table schema.
 */
@Entity
@Table(name = "message_reactions", indexes = {
    @Index(name = "idx_message_reactions_message", columnList = "message_id"),
    @Index(name = "idx_message_reactions_user", columnList = "user_id"),
    @Index(name = "idx_message_reactions_emoji", columnList = "emoji"),
    @Index(name = "idx_message_reactions_created", columnList = "created_at")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_message_reaction", columnNames = {"message_id", "user_id", "emoji"})
})
public class MessageReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user service

    @NotBlank(message = "Emoji is required")
    @Size(max = 10, message = "Emoji must not exceed 10 characters")
    @Column(nullable = false)
    private String emoji;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public MessageReaction() {
        this.createdAt = LocalDateTime.now();
    }

    public MessageReaction(Message message, Long userId, String emoji) {
        this();
        this.message = message;
        this.userId = userId;
        this.emoji = emoji;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "MessageReaction{" +
                "id=" + id +
                ", messageId=" + (message != null ? message.getId() : null) +
                ", userId=" + userId +
                ", emoji='" + emoji + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
