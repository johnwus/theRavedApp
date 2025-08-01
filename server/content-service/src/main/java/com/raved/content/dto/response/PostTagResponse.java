package com.raved.content.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for PostTag entity
 */
public class PostTagResponse {

    private Long id;
    private String tagName;
    private Integer usageCount;
    private Boolean isTrending;
    private LocalDateTime createdAt;

    // Constructors
    public PostTagResponse() {}

    public PostTagResponse(String tagName, Integer usageCount) {
        this.tagName = tagName;
        this.usageCount = usageCount;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Boolean getIsTrending() {
        return isTrending;
    }

    public void setIsTrending(Boolean isTrending) {
        this.isTrending = isTrending;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
