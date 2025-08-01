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
    @Index(name = "idx_chat_room_members_room", columnList = "chat_room_id"),
    @Index(name = "idx_chat_room_members_user", columnList = "user_id"),
    @Index(name = "idx_chat_room_members_role", columnList = "member_role"),
    @Index(name = "idx_chat_room_members_active", columnList = "is_active")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_chat_room_member", columnNames = {"chat_room_id", "user_id"})
})
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user service

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private MemberRole memberRole = MemberRole.MEMBER;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_muted", nullable = false)
    private Boolean isMuted = false;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Column(name = "last_read_at")
    private LocalDateTime lastReadAt;

    @Column(name = "left_at")
    private LocalDateTime leftAt;

    // Enums
    public enum MemberRole {
        ADMIN, MODERATOR, MEMBER
    }

    // Constructors
    public ChatRoomMember() {
        this.joinedAt = LocalDateTime.now();
        this.lastReadAt = LocalDateTime.now();
    }

    public ChatRoomMember(ChatRoom chatRoom, Long userId, MemberRole memberRole) {
        this();
        this.chatRoom = chatRoom;
        this.userId = userId;
        this.memberRole = memberRole;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsMuted() {
        return isMuted;
    }

    public void setIsMuted(Boolean isMuted) {
        this.isMuted = isMuted;
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

    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }

    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();
        this.lastReadAt = LocalDateTime.now();
    }

    // Utility methods
    public void leave() {
        this.isActive = false;
        this.leftAt = LocalDateTime.now();
    }

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
        return memberRole == MemberRole.ADMIN;
    }

    public boolean isModerator() {
        return memberRole == MemberRole.MODERATOR || memberRole == MemberRole.ADMIN;
    }

    public boolean canModerate() {
        return isModerator();
    }

    @Override
    public String toString() {
        return "ChatRoomMember{" +
                "id=" + id +
                ", chatRoomId=" + (chatRoom != null ? chatRoom.getId() : null) +
                ", userId=" + userId +
                ", memberRole=" + memberRole +
                ", isActive=" + isActive +
                ", joinedAt=" + joinedAt +
                '}';
    }
}
