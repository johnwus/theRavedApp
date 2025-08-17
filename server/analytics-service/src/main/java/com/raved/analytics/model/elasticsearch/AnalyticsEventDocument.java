package com.raved.analytics.model.elasticsearch;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Elasticsearch document model for analytics events
 * This model is optimized for real-time analytics and search
 */
@Document(indexName = "analytics-events")
public class AnalyticsEventDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Keyword)
    private String eventType;

    @Field(type = FieldType.Keyword)
    private String entityType;

    @Field(type = FieldType.Keyword)
    private String entityId;

    @Field(type = FieldType.Date)
    private LocalDateTime eventTimestamp;

    @Field(type = FieldType.Keyword)
    private String sessionId;

    @Field(type = FieldType.Text)
    private String userAgent;

    @Field(type = FieldType.Ip)
    private String ipAddress;

    @Field(type = FieldType.Object)
    private Map<String, Object> eventData;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    // Constructors
    public AnalyticsEventDocument() {
    }

    public AnalyticsEventDocument(String id, String userId, String eventType, String entityType,
            String entityId, LocalDateTime eventTimestamp) {
        this.id = id;
        this.userId = userId;
        this.eventType = eventType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.eventTimestamp = eventTimestamp;
        this.createdAt = LocalDateTime.now();
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
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

    public Map<String, Object> getEventData() {
        return eventData;
    }

    public void setEventData(Map<String, Object> eventData) {
        this.eventData = eventData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AnalyticsEventDocument{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId='" + entityId + '\'' +
                ", eventTimestamp=" + eventTimestamp +
                '}';
    }
}
