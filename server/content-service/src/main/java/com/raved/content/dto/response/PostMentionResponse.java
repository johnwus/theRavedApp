package com.raved.content.dto.response;

/**
 * Response DTO for PostMention entity
 */
public class PostMentionResponse {

    private Long id;
    private Long mentionedUserId;
    private String mentionedUsername;
    private String mentionedUserFullName;
    private String mentionedUserProfilePictureUrl;
    private Integer startPosition;
    private Integer endPosition;

    // Constructors
    public PostMentionResponse() {}

    public PostMentionResponse(Long mentionedUserId, String mentionedUsername, Integer startPosition, Integer endPosition) {
        this.mentionedUserId = mentionedUserId;
        this.mentionedUsername = mentionedUsername;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMentionedUserId() {
        return mentionedUserId;
    }

    public void setMentionedUserId(Long mentionedUserId) {
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
}
