package com.raved.social.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for liking/unliking content
 */
public class LikeRequest {

    @NotNull(message = "Entity ID is required")
    private Long entityId;

    @NotBlank(message = "Entity type is required")
    private String entityType; // "post" or "comment"

    // Constructors
    public LikeRequest() {
    }

    public LikeRequest(Long entityId, String entityType) {
        this.entityId = entityId;
        this.entityType = entityType;
    }

    // Getters and Setters
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
}
