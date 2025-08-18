package com.raved.realtime.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * UserPresence Entity for TheRavedApp
 * 
 * Represents user's online status and presence information.
 * Based on the user_presence table schema.
 */
@Entity
@Table(name = "user_presence", indexes = {
    @Index(name = "idx_user_presence_user_id", columnList = "user_id"),
    @Index(name = "idx_user_presence_status", columnList = "status"),
    @Index(name = "idx_user_presence_online", columnList = "is_online"),
    @Index(name = "idx_user_presence_last_active", columnList = "last_active_at"),
    @Index(name = "idx_user_presence_location", columnList = "last_location")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_presence_user", columnNames = {"user_id"})
})
public class UserPresence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // Reference to user service

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PresenceStatus status = PresenceStatus.OFFLINE;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline = false;

    @Column(name = "last_location", length = 255)
    private String lastLocation;

    @Column(name = "device_info", columnDefinition = "TEXT")
    private String deviceInfo;

    @Column(name = "last_active_at", nullable = false)
    private LocalDateTime lastActiveAt;

    @Column(name = "last_seen_at", nullable = false)
    private LocalDateTime lastSeenAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Presence status enum
    public enum PresenceStatus {
        ONLINE, OFFLINE, AWAY, BUSY
    }

    // Constructors
    public UserPresence() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastActiveAt = LocalDateTime.now();
        this.lastSeenAt = LocalDateTime.now();
    }

    public UserPresence(Long userId) {
        this();
        this.userId = userId;
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

    public PresenceStatus getStatus() {
        return status;
    }

    public void setStatus(PresenceStatus status) {
        this.status = status;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public LocalDateTime getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(LocalDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
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
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void markOnline() {
        this.isOnline = true;
        this.status = PresenceStatus.ONLINE;
        this.lastActiveAt = LocalDateTime.now();
        this.lastSeenAt = LocalDateTime.now();
    }

    public void markOffline() {
        this.isOnline = false;
        this.status = PresenceStatus.OFFLINE;
        this.lastSeenAt = LocalDateTime.now();
    }

    public void updateActivity() {
        this.lastActiveAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLocation(String location) {
        this.lastLocation = location;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "UserPresence{" +
                "id=" + id +
                ", userId=" + userId +
                ", status=" + status +
                ", isOnline=" + isOnline +
                ", lastLocation='" + lastLocation + '\'' +
                ", lastActiveAt=" + lastActiveAt +
                ", lastSeenAt=" + lastSeenAt +
                '}';
    }
}
