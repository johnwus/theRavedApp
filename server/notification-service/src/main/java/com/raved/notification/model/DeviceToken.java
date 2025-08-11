package com.raved.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * DeviceToken Entity for TheRavedApp
 * 
 * Represents device tokens for push notifications.
 * Based on the device_tokens table schema.
 */
@Entity
@Table(name = "device_tokens", indexes = {
    @Index(name = "idx_device_tokens_user", columnList = "user_id"),
    @Index(name = "idx_device_tokens_active", columnList = "is_active"),
    @Index(name = "idx_device_tokens_platform", columnList = "platform")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_device_token", columnNames = {"user_id", "token"})
})
public class DeviceToken {

    /**
     * Enum representing the platform type
     */
    public enum Platform {
        IOS, ANDROID, WEB
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user service

    @Column(nullable = false, length = 500)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Platform platform; // IOS, ANDROID, WEB

    @Column(name = "device_info", columnDefinition = "JSONB")
    private String deviceInfo;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_used_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime lastUsedAt;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Constructors
    public DeviceToken() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastUsedAt = LocalDateTime.now();
    }

    public DeviceToken(Long userId, String token, Platform platform) {
        this();
        this.userId = userId;
        this.token = token;
        this.platform = platform;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
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

    // Lifecycle methods
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.lastUsedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public void updateLastUsed() {
        this.lastUsedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public boolean isExpired(int daysThreshold) {
        return this.lastUsedAt.plusDays(daysThreshold).isBefore(LocalDateTime.now());
    }

    public boolean isIOS() {
        return Platform.IOS.equals(this.platform);
    }

    public boolean isAndroid() {
        return Platform.ANDROID.equals(this.platform);
    }

    public boolean isWeb() {
        return Platform.WEB.equals(this.platform);
    }

    @Override
    public String toString() {
        return "DeviceToken{" +
                "id=" + id +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", platform=" + platform +
                ", isActive=" + isActive +
                ", lastUsedAt=" + lastUsedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
