package com.raved.realtime.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for joining a chat room
 */
public class JoinChatRoomRequest {

    @NotNull(message = "Chat room ID is required")
    private Long chatRoomId;

    private String joinCode; // For private rooms

    // Constructors
    public JoinChatRoomRequest() {
    }

    public JoinChatRoomRequest(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public JoinChatRoomRequest(Long chatRoomId, String joinCode) {
        this.chatRoomId = chatRoomId;
        this.joinCode = joinCode;
    }

    // Getters and Setters
    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }
}
