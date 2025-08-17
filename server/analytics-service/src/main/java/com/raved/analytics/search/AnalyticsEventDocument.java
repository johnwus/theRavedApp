package com.raved.analytics.search;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "analytics-events")
public class AnalyticsEventDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Keyword)
    private String sessionId;

    @Field(type = FieldType.Keyword)
    private String eventType;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String hashtags;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String contentTags;

    @Field(type = FieldType.Keyword)
    private String eventDate;

    @Field(type = FieldType.Integer)
    private Integer eventHour;

    @Field(type = FieldType.Date)
    private LocalDateTime eventTimestamp;

    @Field(type = FieldType.Keyword)
    private String platform;

    @Field(type = FieldType.Keyword)
    private String deviceType;

    @Field(type = FieldType.Keyword)
    private String entityType;

    @Field(type = FieldType.Keyword)
    private String entityId;

    // Ecommerce fields
    @Field(type = FieldType.Keyword)
    private String productId;

    @Field(type = FieldType.Text)
    private String productName;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Keyword)
    private String currency;

    // Social fields
    @Field(type = FieldType.Keyword)
    private String postId;

    @Field(type = FieldType.Keyword)
    private String interactionType;

    // Content fields
    @Field(type = FieldType.Keyword)
    private String contentType;

    @Field(type = FieldType.Keyword)
    private String contentCategory;

    // Location fields
    @Field(type = FieldType.Keyword)
    private String country;

    @Field(type = FieldType.Keyword)
    private String city;

    // Getters and setters
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getContentTags() {
        return contentTags;
    }

    public void setContentTags(String contentTags) {
        this.contentTags = contentTags;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getEventHour() {
        return eventHour;
    }

    public void setEventHour(Integer eventHour) {
        this.eventHour = eventHour;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(String contentCategory) {
        this.contentCategory = contentCategory;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
