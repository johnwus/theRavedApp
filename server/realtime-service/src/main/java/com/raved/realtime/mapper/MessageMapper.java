package com.raved.realtime.mapper;

import com.raved.realtime.dto.request.SendMessageRequest;
import com.raved.realtime.dto.response.MessageResponse;
import com.raved.realtime.model.Message;
import org.springframework.stereotype.Component;

/**
 * Mapper for Message entities and DTOs
 */
@Component
public class MessageMapper {

    /**
     * Convert SendMessageRequest to Message entity
     */
    public Message toMessage(SendMessageRequest request) {
        if (request == null) {
            return null;
        }
        
        Message message = new Message();
        message.setSenderId(request.getSenderId());
        message.setContent(request.getContent());
        message.setType(request.getType());
        message.setAttachmentUrl(request.getAttachmentUrl());
        message.setMetadata(request.getMetadata());
        message.setIsEdited(false);
        message.setIsDeleted(false);
        
        return message;
    }

    /**
     * Convert Message entity to MessageResponse DTO
     */
    public MessageResponse toMessageResponse(Message message) {
        if (message == null) {
            return null;
        }
        
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setMessageId(message.getMessageId());
        response.setChatRoomId(message.getChatRoomId());
        response.setSenderId(message.getSenderId());
        response.setContent(message.getContent());
        response.setType(message.getType());
        response.setStatus(message.getStatus());
        response.setIsEdited(message.getIsEdited());
        response.setIsDeleted(message.getIsDeleted());
        response.setAttachmentUrl(message.getAttachmentUrl());
        response.setMetadata(message.getMetadata());
        response.setDeliveredAt(message.getDeliveredAt());
        response.setReadAt(message.getReadAt());
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());
        
        return response;
    }
}