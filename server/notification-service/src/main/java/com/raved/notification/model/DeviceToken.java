package com.raved.notification.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DeviceToken Document for TheRavedApp MongoDB
 *
 * Represents device tokens for push notifications.
 * Converted from JPA entity to
 * MongoDB document.
 */
@Document(collection = "device_tokens")
@CompoundIndexes({
    @CompoundIndex(name = "idx_user_token", def = "{'userId': 1, 'token': 1}", unique = true),
    @CompoundIndex(name = "idx_user_active", def = "{'userId': 1, 'isActive': 1}"),
    @CompoundIndex(name = "idx_platform_active", def = "{'platform': 1, 'isActive': 1}")
})
public class DeviceToken {

    /**
     * Enum representing the platform type
     */
    public enum Platform {
        IOS, ANDROID, WEB
    }

    @Id
    private String id;

    @Field("userId")
    @Indexed
    @NotNull
    private String userId; // Reference to user service (changed to String for MongoDB)

    @Field("token")
    @NotBlank
    private String token;

    @Field("platform")
    @Indexed
    @NotNull
    private Platform platform; // IOS, ANDROID, WEB

    @Field("deviceInfo")
    private Map<String, Object> deviceInfo; // Changed to Map for MongoDB

    @Field("isActive")
    @Indexed
    private Boolean isActive = true;

    @Field("lastUsedAt")
    private LocalDateTime lastUsedAt;

    @Field("createdAt")
    @Indexed
    private LocalDateTime createdAt;

    @Field("updatedAt")
    private LocalDateTime updatedAt;

    // Constructors
    public DeviceToken() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastUsedAt = LocalDateTime.now();
    }

    public DeviceToken(String userId, String token, Platform platform) {
        this();
        this.userId = userId;
        this.token = token;
        this.platform = platform;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public Map<String, Object> getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(Map<String, Object> deviceInfo) {
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

    // Lifecycle methods for MongoDB
    public void initializeTimestamps() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
        if (this.lastUsedAt == null) {
            this.lastUsedAt = now;
        }
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
