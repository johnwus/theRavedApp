package com.raved.events.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for event responses
 */
public class EventResponse {

    private Long id;
    private Long organizerId;
    private String title;
    private String description;
    private String imageUrl;
    private String orgAvatarUrl;
    private String locationName;
    private String locationGeo;
    private Instant startsAt;
    private Instant endsAt;
    private String category;
    private String audience;
    private BigDecimal priceAmount;
    private String currency;
    private Integer capacity;
    private Boolean isFeatured;
    private Instant createdAt;
    private Instant updatedAt;

    // Additional computed fields
    private Integer attendeeCount;
    private Boolean isUserAttending;
    private Boolean isUserInterested;

    // Constructors
    public EventResponse() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOrgAvatarUrl() {
        return orgAvatarUrl;
    }

    public void setOrgAvatarUrl(String orgAvatarUrl) {
        this.orgAvatarUrl = orgAvatarUrl;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationGeo() {
        return locationGeo;
    }

    public void setLocationGeo(String locationGeo) {
        this.locationGeo = locationGeo;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(BigDecimal priceAmount) {
        this.priceAmount = priceAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(Integer attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public Boolean getIsUserAttending() {
        return isUserAttending;
    }

    public void setIsUserAttending(Boolean isUserAttending) {
        this.isUserAttending = isUserAttending;
    }

    public Boolean getIsUserInterested() {
        return isUserInterested;
    }

    public void setIsUserInterested(Boolean isUserInterested) {
        this.isUserInterested = isUserInterested;
    }
}
