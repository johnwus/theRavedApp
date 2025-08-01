package com.raved.notification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Index(name = "idx_device_tokens_token", columnList = "token"),
    @Index(name = "idx_device_tokens_platform", columnList = "platform"),
    @Index(name = "idx_device_tokens_active", columnList = "is_active"),
    @Index(name = "idx_device_tokens_last_used", columnList = "last_used_at")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_device_token", columnNames = {"user_id", "token"})
})
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to user service

    @NotBlank(message = "Token is required")
    @Size(max = 500, message = "Token must not exceed 500 characters")
    @Column(nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Size(max = 255, message = "Device info must not exceed 255 characters")
    @Column(name = "device_info")
    private String deviceInfo;

    @Size(max = 100, message = "App version must not exceed 100 characters")
    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Enums
    public enum Platform {
        IOS, ANDROID, WEB
    }

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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastUsedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Utility methods
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
        if (lastUsedAt == null) return false;
        return lastUsedAt.isBefore(LocalDateTime.now().minusDays(daysThreshold));
    }

    public boolean isIOS() {
        return platform == Platform.IOS;
    }

    public boolean isAndroid() {
        return platform == Platform.ANDROID;
    }

    public boolean isWeb() {
        return platform == Platform.WEB;
    }

    @Override
    public String toString() {
        return "DeviceToken{" +
                "id=" + id +
                ", userId=" + userId +
                ", platform=" + platform +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", isActive=" + isActive +
                ", lastUsedAt=" + lastUsedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
