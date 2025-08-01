package com.raved.realtime.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Request DTO for creating a new chat room
 */
public class CreateChatRoomRequest {

    @NotBlank(message = "Chat room name is required")
    @Size(max = 255, message = "Chat room name must not exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private String roomType; // "direct", "group", "public", "private"

    private Boolean isPrivate = false;

    private Integer maxMembers;

    private List<Long> initialMemberIds;

    private String avatarUrl;

    // Constructors
    public CreateChatRoomRequest() {
    }

    public CreateChatRoomRequest(String name, String roomType) {
        this.name = name;
        this.roomType = roomType;
    }

    // Getters and Setters
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

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }

    public List<Long> getInitialMemberIds() {
        return initialMemberIds;
    }

    public void setInitialMemberIds(List<Long> initialMemberIds) {
        this.initialMemberIds = initialMemberIds;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
