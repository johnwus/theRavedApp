package com.raved.realtime.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for adding/removing message reactions
 */
public class MessageReactionRequest {

    @NotNull(message = "Message ID is required")
    private Long messageId;

    @NotBlank(message = "Reaction type is required")
    private String reactionType; // "like", "love", "laugh", "angry", "sad", "wow"

    // Constructors
    public MessageReactionRequest() {
    }

    public MessageReactionRequest(Long messageId, String reactionType) {
        this.messageId = messageId;
        this.reactionType = reactionType;
    }

    // Getters and Setters
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }
}
