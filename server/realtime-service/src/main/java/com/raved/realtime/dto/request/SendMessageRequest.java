package com.raved.realtime.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Request DTO for sending a message
 */
public class SendMessageRequest {

    @NotNull(message = "Chat room ID is required")
    private Long chatRoomId;

    @NotBlank(message = "Message content is required")
    @Size(max = 4000, message = "Message content must not exceed 4000 characters")
    private String content;

    private String messageType; // "text", "image", "file", "audio", "video", "system"

    private Long replyToMessageId; // For threaded messages

    private List<String> mediaUrls;

    private String metadata; // JSON string for additional data

    // Constructors
    public SendMessageRequest() {
    }

    public SendMessageRequest(Long chatRoomId, String content) {
        this.chatRoomId = chatRoomId;
        this.content = content;
        this.messageType = "text";
    }

    // Getters and Setters
    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Long getReplyToMessageId() {
        return replyToMessageId;
    }

    public void setReplyToMessageId(Long replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
