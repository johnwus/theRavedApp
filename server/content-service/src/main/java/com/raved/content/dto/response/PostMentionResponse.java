package com.raved.content.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for PostMention MongoDB document
 */
public class PostMentionResponse {

    private String id;
    private String mentionedUserId;
    private String mentionedUsername;
    private String mentionedUserFullName;
    private String mentionedUserProfilePictureUrl;
    private Integer startPosition;
    private Integer endPosition;
    private LocalDateTime createdAt;
    private String status;

    // Constructors
    public PostMentionResponse() {
    }

    public PostMentionResponse(String mentionedUserId, String mentionedUsername, Integer startPosition,
            Integer endPosition) {
        this.mentionedUserId = mentionedUserId;
        this.mentionedUsername = mentionedUsername;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMentionedUserId() {
        return mentionedUserId;
    }

    public void setMentionedUserId(String mentionedUserId) {
        this.mentionedUserId = mentionedUserId;
    }

    public String getMentionedUsername() {
        return mentionedUsername;
    }

    public void setMentionedUsername(String mentionedUsername) {
        this.mentionedUsername = mentionedUsername;
    }

    public String getMentionedUserFullName() {
        return mentionedUserFullName;
    }

    public void setMentionedUserFullName(String mentionedUserFullName) {
        this.mentionedUserFullName = mentionedUserFullName;
    }

    public String getMentionedUserProfilePictureUrl() {
        return mentionedUserProfilePictureUrl;
    }

    public void setMentionedUserProfilePictureUrl(String mentionedUserProfilePictureUrl) {
        this.mentionedUserProfilePictureUrl = mentionedUserProfilePictureUrl;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
