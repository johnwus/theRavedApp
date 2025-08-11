package com.raved.realtime.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a chat message
 */
@Entity
@Table(name = "messages", indexes = {
    @Index(name = "idx_messages_room_id", columnList = "room_id"),
    @Index(name = "idx_messages_sender_id", columnList = "sender_id"),
    @Index(name = "idx_messages_created_at", columnList = "created_at"),
    @Index(name = "idx_messages_reply_to", columnList = "reply_to_message_id"),
    @Index(name = "idx_messages_room_time", columnList = "room_id, created_at DESC")
})
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "message_id", unique = true, nullable = false)
    private String messageId; // UUID for external reference
    
    @Column(name = "room_id", nullable = false)
    private Long roomId; // Reference to chat_rooms(id)
    
    @Column(name = "chat_room_id", nullable = false)
    private Long chatRoomId; // Reference to chat_rooms(id) - alternative field name
    
    @Column(name = "sender_id", nullable = false)
    private Long senderId; // Reference to user service
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private MessageType type = MessageType.TEXT; // TEXT, IMAGE, VIDEO, AUDIO, FILE, SYSTEM
    
    @Column(name = "message_type", nullable = false, length = 20)
    private String messageType = "TEXT"; // TEXT, IMAGE, VIDEO, AUDIO, FILE, SYSTEM
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MessageStatus status = MessageStatus.SENT; // SENT, DELIVERED, READ
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "attachment_url", columnDefinition = "TEXT")
    private String attachmentUrl; // URL to attached file
    
    @Column(name = "media_url", columnDefinition = "TEXT")
    private String mediaUrl;
    
    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata; // JSON metadata for the message
    
    @Column(name = "media_metadata", columnDefinition = "JSONB")
    private String mediaMetadata; // file size, dimensions, duration, etc.
    
    @Column(name = "reply_to_message_id")
    private Long replyToMessageId; // Reference to messages(id)
    
    // System fields
    @Column(name = "is_edited", nullable = false)
    private Boolean isEdited = false;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    
    @Column(name = "edited_at")
    private LocalDateTime editedAt;
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Constructors
    public Message() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isEdited = false;
        this.isDeleted = false;
        this.status = MessageStatus.SENT;
    }

    public Message(Long roomId, Long senderId, String content) {
        this();
        this.roomId = roomId;
        this.chatRoomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.messageType = "TEXT";
        this.type = MessageType.TEXT;
    }

    public Message(Long roomId, Long senderId, String messageType, String content) {
        this();
        this.roomId = roomId;
        this.chatRoomId = roomId;
        this.senderId = senderId;
        this.messageType = messageType;
        this.content = content;
        this.type = MessageType.valueOf(messageType);
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
        this.chatRoomId = roomId;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
        this.roomId = chatRoomId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
        this.messageType = type.name();
        this.updatedAt = LocalDateTime.now();
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
        this.type = MessageType.valueOf(messageType);
        this.updatedAt = LocalDateTime.now();
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
        this.updatedAt = LocalDateTime.now();
    }

    public String getMediaMetadata() {
        return mediaMetadata;
    }

    public void setMediaMetadata(String mediaMetadata) {
        this.mediaMetadata = mediaMetadata;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getReplyToMessageId() {
        return replyToMessageId;
    }

    public void setReplyToMessageId(Long replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
        if (isEdited) {
            this.editedAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility methods
    public void editContent(String newContent) {
        this.content = newContent;
        this.isEdited = true;
        this.editedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void restore() {
        this.isDeleted = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsDelivered() {
        this.status = MessageStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.status = MessageStatus.READ;
        this.readAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isTextMessage() {
        return MessageType.TEXT.equals(this.type);
    }

    public boolean isMediaMessage() {
        return MessageType.IMAGE.equals(this.type) || 
               MessageType.VIDEO.equals(this.type) || 
               MessageType.AUDIO.equals(this.type) || 
               MessageType.FILE.equals(this.type);
    }

    public boolean isSystemMessage() {
        return MessageType.SYSTEM.equals(this.type);
    }

    public boolean isReply() {
        return this.replyToMessageId != null;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", messageId='" + messageId + '\'' +
                ", roomId=" + roomId +
                ", chatRoomId=" + chatRoomId +
                ", senderId=" + senderId +
                ", type=" + type +
                ", messageType='" + messageType + '\'' +
                ", status=" + status +
                ", content='" + (content != null ? content.substring(0, Math.min(content.length(), 50)) + "..." : null) + '\'' +
                ", isEdited=" + isEdited +
                ", isDeleted=" + isDeleted +
                ", createdAt=" + createdAt +
                '}';
    }
}