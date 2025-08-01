package com.raved.analytics.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * Request DTO for tracking analytics events
 */
public class TrackEventRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Event type is required")
    private String eventType; // "page_view", "post_like", "post_share", "product_view", "purchase", etc.

    @NotBlank(message = "Event name is required")
    private String eventName;

    private Long entityId; // ID of the entity being tracked (post, product, user, etc.)

    private String entityType; // "post", "product", "user", "comment", etc.

    private Map<String, Object> properties; // Additional event properties

    private String sessionId;

    private String userAgent;

    private String ipAddress;

    private String referrer;

    private String platform; // "web", "mobile", "ios", "android"

    // Constructors
    public TrackEventRequest() {
    }

    public TrackEventRequest(Long userId, String eventType, String eventName) {
        this.userId = userId;
        this.eventType = eventType;
        this.eventName = eventName;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
