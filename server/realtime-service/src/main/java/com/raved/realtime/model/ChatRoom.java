package com.raved.realtime.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a chat room
 */
@Entity
@Table(name = "chat_rooms", indexes = {
    @Index(name = "idx_chat_rooms_type", columnList = "room_type"),
    @Index(name = "idx_chat_rooms_created_by", columnList = "created_by"),
    @Index(name = "idx_chat_rooms_faculty", columnList = "faculty_id"),
    @Index(name = "idx_chat_rooms_active", columnList = "is_active"),
    @Index(name = "idx_chat_rooms_last_message", columnList = "last_message_at")
})
public class ChatRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "room_id", unique = true, nullable = false)
    private String roomId; // UUID for external reference
    
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false, length = 20)
    private ChatRoomType roomType; // DIRECT, GROUP, FACULTY_GROUP
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ChatRoomType type; // DIRECT, GROUP, FACULTY_GROUP
    
    @Column(length = 255)
    private String name; // null for direct messages
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;
    
    @Column(name = "created_by", nullable = false)
    private Long createdBy; // Reference to user service
    
    @Column(name = "faculty_id")
    private Long facultyId; // Reference to user service (for faculty groups)
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate = false;
    
    @Column(name = "max_participants")
    private Integer maxParticipants = 100;
    
    @Column(name = "current_participants")
    private Integer currentParticipants = 0;
    
    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;
    
    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;
    
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Constructors
    public ChatRoom() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = true;
        this.isPrivate = false;
        this.maxParticipants = 100;
        this.currentParticipants = 0;
    }

    public ChatRoom(ChatRoomType roomType, Long createdBy) {
        this();
        this.roomType = roomType;
        this.type = roomType;
        this.createdBy = createdBy;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
        this.updatedAt = LocalDateTime.now();
    }

    public ChatRoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(ChatRoomType roomType) {
        this.roomType = roomType;
        this.type = roomType;
        this.updatedAt = LocalDateTime.now();
    }

    public ChatRoomType getType() {
        return type;
    }

    public void setType(ChatRoomType type) {
        this.type = type;
        this.roomType = type;
        this.updatedAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
        this.updatedAt = LocalDateTime.now();
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

    // Utility methods
    public void updateLastMessage() {
        this.lastMessageAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLastActivity() {
        this.lastActivityAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addParticipant() {
        if (this.currentParticipants < this.maxParticipants) {
            this.currentParticipants++;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void removeParticipant() {
        if (this.currentParticipants > 0) {
            this.currentParticipants--;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isDirectMessage() {
        return ChatRoomType.DIRECT.equals(this.type);
    }

    public boolean isGroupChat() {
        return ChatRoomType.GROUP.equals(this.type) || ChatRoomType.FACULTY_GROUP.equals(this.type);
    }

    public boolean isFacultyGroup() {
        return ChatRoomType.FACULTY_GROUP.equals(this.type);
    }

    public boolean hasAvailableSlots() {
        return this.currentParticipants < this.maxParticipants;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", roomId='" + roomId + '\'' +
                ", roomType=" + roomType +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", createdBy=" + createdBy +
                ", isActive=" + isActive +
                ", isPrivate=" + isPrivate +
                ", maxParticipants=" + maxParticipants +
                ", currentParticipants=" + currentParticipants +
                ", lastMessageAt=" + lastMessageAt +
                ", lastActivityAt=" + lastActivityAt +
                '}';
    }
}