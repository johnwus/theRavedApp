package com.raved.realtime.mapper;

import com.raved.realtime.dto.request.CreateChatRoomRequest;
import com.raved.realtime.dto.response.ChatRoomResponse;
import com.raved.realtime.model.ChatRoom;
import org.springframework.stereotype.Component;

/**
 * Mapper for ChatRoom entities and DTOs
 */
@Component
public class ChatRoomMapper {

    /**
     * Convert CreateChatRoomRequest to ChatRoom entity
     */
    public ChatRoom toChatRoom(CreateChatRoomRequest request) {
        if (request == null) {
            return null;
        }
        
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(request.getName());
        chatRoom.setDescription(request.getDescription());
        chatRoom.setType(request.getType());
        chatRoom.setCreatedBy(request.getCreatedBy());
        chatRoom.setIsPrivate(request.getIsPrivate() != null ? request.getIsPrivate() : false);
        
        return chatRoom;
    }

    /**
     * Convert ChatRoom entity to ChatRoomResponse DTO
     */
    public ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom) {
        if (chatRoom == null) {
            return null;
        }
        
        ChatRoomResponse response = new ChatRoomResponse();
        response.setId(chatRoom.getId());
        response.setRoomId(chatRoom.getRoomId());
        response.setName(chatRoom.getName());
        response.setDescription(chatRoom.getDescription());
        response.setType(chatRoom.getType());
        response.setCreatedBy(chatRoom.getCreatedBy());
        response.setIsPrivate(chatRoom.getIsPrivate());
        response.setIsActive(chatRoom.getIsActive());
        response.setMaxParticipants(chatRoom.getMaxParticipants());
        response.setCurrentParticipants(chatRoom.getCurrentParticipants());
        response.setCreatedAt(chatRoom.getCreatedAt());
        response.setUpdatedAt(chatRoom.getUpdatedAt());
        response.setLastActivityAt(chatRoom.getLastActivityAt());
        
        return response;
    }
}