package com.raved.analytics.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * AnalyticsEvent Entity for TheRavedApp
 *
 * Represents analytics events for tracking user behavior and system metrics.
 * Based on the analytics_events table schema.
 */
@Entity
@Table(name = "analytics_events", indexes = {
        @Index(name = "idx_analytics_events_user", columnList = "user_id"),
        @Index(name = "idx_analytics_events_type", columnList = "event_type"),
        @Index(name = "idx_analytics_events_entity", columnList = "entity_type, entity_id"),
        @Index(name = "idx_analytics_events_timestamp", columnList = "event_timestamp"),
        @Index(name = "idx_analytics_events_session", columnList = "session_id"),
        @Index(name = "idx_analytics_events_user_type", columnList = "user_id, event_type"),
        @Index(name = "idx_analytics_events_date", columnList = "event_date")
})
public class AnalyticsEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // Reference to user service, nullable for anonymous events

    @Column(name = "session_id")
    private String sessionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Size(max = 100, message = "Entity type must not exceed 100 characters")
    @Column(name = "entity_type")
    private String entityType; // e.g., "post", "product", "user"

    @Column(name = "entity_id")
    private Long entityId; // ID of the entity being tracked

    @Column(name = "event_data", columnDefinition = "TEXT")
    private String eventData; // JSON string for additional event data

    @Size(max = 45, message = "IP address must not exceed 45 characters")
    @Column(name = "ip_address")
    private String ipAddress;

    @Size(max = 500, message = "User agent must not exceed 500 characters")
    @Column(name = "user_agent")
    private String userAgent;

    @Size(max = 10, message = "Platform must not exceed 10 characters")
    private String platform; // "web", "ios", "android"

    @Size(max = 100, message = "Device type must not exceed 100 characters")
    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime eventTimestamp;

    @Column(name = "event_date", nullable = false)
    private java.sql.Date eventDate; // For efficient date-based queries

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Enums
    public enum EventType {
        // User Events
        USER_LOGIN, USER_LOGOUT, USER_REGISTRATION, USER_PROFILE_VIEW, USER_PROFILE_UPDATE,

        // Content Events
        POST_VIEW, POST_LIKE, POST_UNLIKE, POST_SHARE, POST_COMMENT, POST_CREATE,

        // Product Events
        PRODUCT_VIEW, PRODUCT_LIKE, PRODUCT_PURCHASE, PRODUCT_SEARCH,

        // Chat Events
        MESSAGE_SENT, CHAT_ROOM_JOIN, CHAT_ROOM_LEAVE,

        // Navigation Events
        PAGE_VIEW, BUTTON_CLICK, SEARCH_PERFORMED,

        // System Events
        ERROR_OCCURRED, PERFORMANCE_METRIC
    }

    // Constructors
    public AnalyticsEvent() {
        this.eventTimestamp = LocalDateTime.now();
        this.eventDate = java.sql.Date.valueOf(this.eventTimestamp.toLocalDate());
        this.createdAt = LocalDateTime.now();
    }

    public AnalyticsEvent(Long userId, EventType eventType, String entityType, Long entityId) {
        this();
        this.userId = userId;
        this.eventType = eventType;
        this.entityType = entityType;
        this.entityId = entityId;
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
        this.eventDate = java.sql.Date.valueOf(eventTimestamp.toLocalDate());
    }

    public java.sql.Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(java.sql.Date eventDate) {
        this.eventDate = eventDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (this.eventTimestamp == null) {
            this.eventTimestamp = LocalDateTime.now();
        }
        this.eventDate = java.sql.Date.valueOf(this.eventTimestamp.toLocalDate());
        this.createdAt = LocalDateTime.now();
    }

    // Utility methods
    public boolean isUserEvent() {
        return userId != null;
    }

    public boolean isAnonymousEvent() {
        return userId == null;
    }

    public boolean isContentEvent() {
        return eventType.name().contains("POST") || eventType.name().contains("PRODUCT");
    }

    public boolean isMobileEvent() {
        return "ios".equals(platform) || "android".equals(platform);
    }

    public boolean isWebEvent() {
        return "web".equals(platform);
    }

    @Override
    public String toString() {
        return "AnalyticsEvent{" +
                "id=" + id +
                ", userId=" + userId +
                ", eventType=" + eventType +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", platform='" + platform + '\'' +
                ", eventTimestamp=" + eventTimestamp +
                '}';
    }
}
