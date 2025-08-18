package com.raved.realtime.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for joining a chat room
 */
public class JoinChatRoomRequest {
    
    @NotBlank(message = "Room ID is required")
    private String roomId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    // Getters and setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}