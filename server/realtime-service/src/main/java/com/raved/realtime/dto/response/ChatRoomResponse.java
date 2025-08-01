package com.raved.realtime.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for ChatRoom entity
 */
public class ChatRoomResponse {

    private Long id;
    private String name;
    private String description;
    private String roomType; // "direct", "group", "public", "private"
    private Boolean isPrivate;
    private String avatarUrl;
    private Long createdByUserId;
    private String createdByUsername;
    private Integer maxMembers;
    private Integer currentMemberCount;
    private Boolean isActive;

    // Last message info
    private String lastMessageContent;
    private String lastMessageSenderName;
    private LocalDateTime lastMessageAt;

    // User context
    private Boolean isCurrentUserMember;
    private Boolean isCurrentUserAdmin;
    private Boolean hasUnreadMessages;
    private Integer unreadMessageCount;

    // Members (for small groups)
    private List<ChatRoomMemberResponse> members;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ChatRoomResponse() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Integer getCurrentMemberCount() {
        return currentMemberCount;
    }

    public void setCurrentMemberCount(Integer currentMemberCount) {
        this.currentMemberCount = currentMemberCount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public String getLastMessageSenderName() {
        return lastMessageSenderName;
    }

    public void setLastMessageSenderName(String lastMessageSenderName) {
        this.lastMessageSenderName = lastMessageSenderName;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public Boolean getIsCurrentUserMember() {
        return isCurrentUserMember;
    }

    public void setIsCurrentUserMember(Boolean isCurrentUserMember) {
        this.isCurrentUserMember = isCurrentUserMember;
    }

    public Boolean getIsCurrentUserAdmin() {
        return isCurrentUserAdmin;
    }

    public void setIsCurrentUserAdmin(Boolean isCurrentUserAdmin) {
        this.isCurrentUserAdmin = isCurrentUserAdmin;
    }

    public Boolean getHasUnreadMessages() {
        return hasUnreadMessages;
    }

    public void setHasUnreadMessages(Boolean hasUnreadMessages) {
        this.hasUnreadMessages = hasUnreadMessages;
    }

    public Integer getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(Integer unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }
}
