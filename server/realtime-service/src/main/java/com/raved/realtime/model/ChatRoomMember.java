package com.raved.realtime.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * ChatRoomMember Entity for TheRavedApp
 * 
 * Represents membership in chat rooms.
 * Based on the chat_room_members table schema.
 */
@Entity
@Table(name = "chat_room_members", indexes = {
    @Index(name = "idx_chat_members_room", columnList = "room_id"),
    @Index(name = "idx_chat_members_user", columnList = "user_id"),
    @Index(name = "idx_chat_members_active", columnList = "is_active")
}, uniqueConstraints = {
    @UniqueConstraint(name = "idx_chat_members_room_user", columnNames = {"room_id", "user_id"})
})
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId; // Reference to chat_rooms(id)

    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user service

    @Column(length = 20)
    private String role = "MEMBER"; // ADMIN, MODERATOR, MEMBER

    @Column(name = "joined_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime joinedAt;

    @Column(name = "last_read_at")
    private LocalDateTime lastReadAt;

    @Column(name = "is_muted")
    private Boolean isMuted = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Constructors
    public ChatRoomMember() {
        this.joinedAt = LocalDateTime.now();
    }

    public ChatRoomMember(Long roomId, Long userId, String role) {
        this();
        this.roomId = roomId;
        this.userId = userId;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(LocalDateTime lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public Boolean getIsMuted() {
        return isMuted;
    }

    public void setIsMuted(Boolean isMuted) {
        this.isMuted = isMuted;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Utility methods
    public void updateLastRead() {
        this.lastReadAt = LocalDateTime.now();
    }

    public void mute() {
        this.isMuted = true;
    }

    public void unmute() {
        this.isMuted = false;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public boolean isModerator() {
        return "MODERATOR".equals(role) || "ADMIN".equals(role);
    }

    public boolean canModerate() {
        return isModerator();
    }

    @Override
    public String toString() {
        return "ChatRoomMember{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", userId=" + userId +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", joinedAt=" + joinedAt +
                '}';
    }
}
