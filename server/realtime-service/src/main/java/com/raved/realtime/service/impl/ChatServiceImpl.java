package com.raved.realtime.service.impl;

import com.raved.realtime.dto.request.CreateChatRoomRequest;
import com.raved.realtime.dto.request.JoinChatRoomRequest;
import com.raved.realtime.dto.response.ChatRoomResponse;
import com.raved.realtime.exception.ChatRoomNotFoundException;
import com.raved.realtime.exception.UnauthorizedChatAccessException;
import com.raved.realtime.mapper.ChatRoomMapper;
import com.raved.realtime.model.ChatRoom;
import com.raved.realtime.model.ChatRoomType;
import com.raved.realtime.repository.ChatRoomRepository;
import com.raved.realtime.service.ChatService;
import com.raved.realtime.service.MessageService;
import com.raved.realtime.service.PresenceService;
import com.raved.realtime.websocket.MessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of ChatService
 */
@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMapper chatRoomMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PresenceService presenceService;

    @Autowired
    private MessageBroker messageBroker;

    @Override
    public ChatRoomResponse createChatRoom(CreateChatRoomRequest request) {
        logger.info("Creating chat room: {} by user: {}", request.getName(), request.getCreatedBy());
        
        ChatRoom chatRoom = chatRoomMapper.toChatRoom(request);
        chatRoom.setRoomId(generateRoomId());
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setUpdatedAt(LocalDateTime.now());
        chatRoom.setIsActive(true);
        
        // Set default values based on room type
        if (request.getType() == ChatRoomType.PRIVATE) {
            chatRoom.setMaxParticipants(2);
        } else if (request.getType() == ChatRoomType.GROUP) {
            chatRoom.setMaxParticipants(request.getMaxParticipants() != null ? request.getMaxParticipants() : 50);
        } else if (request.getType() == ChatRoomType.PUBLIC) {
            chatRoom.setMaxParticipants(1000); // Large limit for public rooms
        }
        
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        
        // Add creator as first participant
        addParticipant(savedChatRoom.getId(), request.getCreatedBy());
        
        logger.info("Chat room created: {}", savedChatRoom.getId());
        return chatRoomMapper.toChatRoomResponse(savedChatRoom);
    }

    @Override
    public ChatRoomResponse joinChatRoom(JoinChatRoomRequest request) {
        logger.info("User {} joining chat room: {}", request.getUserId(), request.getRoomId());
        
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(request.getRoomId());
        if (chatRoomOpt.isEmpty()) {
            throw new ChatRoomNotFoundException("Chat room not found: " + request.getRoomId());
        }
        
        ChatRoom chatRoom = chatRoomOpt.get();
        
        // Check if room is active
        if (!chatRoom.getIsActive()) {
            throw new UnauthorizedChatAccessException("Chat room is not active");
        }
        
        // Check if user can join
        if (!canUserJoinRoom(chatRoom, request.getUserId())) {
            throw new UnauthorizedChatAccessException("User cannot join this chat room");
        }
        
        // Check participant limit
        if (chatRoom.getCurrentParticipants() >= chatRoom.getMaxParticipants()) {
            throw new UnauthorizedChatAccessException("Chat room is full");
        }
        
        // Add participant
        addParticipant(chatRoom.getId(), request.getUserId());
        
        // Update room stats
        chatRoom.setCurrentParticipants(chatRoom.getCurrentParticipants() + 1);
        chatRoom.setLastActivityAt(LocalDateTime.now());
        chatRoom.setUpdatedAt(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
        
        // Notify other participants
        messageBroker.broadcastToRoom(request.getRoomId(), "USER_JOINED", 
                "User " + request.getUserId() + " joined the room");
        
        logger.info("User {} joined chat room: {}", request.getUserId(), request.getRoomId());
        return chatRoomMapper.toChatRoomResponse(chatRoom);
    }

    @Override
    public void leaveChatRoom(String roomId, Long userId) {
        logger.info("User {} leaving chat room: {}", userId, roomId);
        
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new ChatRoomNotFoundException("Chat room not found: " + roomId);
        }
        
        ChatRoom chatRoom = chatRoomOpt.get();
        
        // Remove participant
        removeParticipant(chatRoom.getId(), userId);
        
        // Update room stats
        chatRoom.setCurrentParticipants(Math.max(0, chatRoom.getCurrentParticipants() - 1));
        chatRoom.setLastActivityAt(LocalDateTime.now());
        chatRoom.setUpdatedAt(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
        
        // Notify other participants
        messageBroker.broadcastToRoom(roomId, "USER_LEFT", 
                "User " + userId + " left the room");
        
        logger.info("User {} left chat room: {}", userId, roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public ChatRoomResponse getChatRoom(String roomId) {
        logger.debug("Getting chat room: {}", roomId);
        
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new ChatRoomNotFoundException("Chat room not found: " + roomId);
        }
        
        return chatRoomMapper.toChatRoomResponse(chatRoomOpt.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatRoomResponse> getUserChatRooms(Long userId, Pageable pageable) {
        logger.debug("Getting chat rooms for user: {}", userId);
        
        Page<ChatRoom> chatRooms = chatRoomRepository.findByParticipantsContainingOrderByLastActivityAtDesc(
                userId, pageable);
        
        return chatRooms.map(chatRoomMapper::toChatRoomResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatRoomResponse> getPublicChatRooms(Pageable pageable) {
        logger.debug("Getting public chat rooms");
        
        Page<ChatRoom> publicRooms = chatRoomRepository.findByTypeAndIsActiveTrueOrderByCurrentParticipantsDesc(
                ChatRoomType.PUBLIC, pageable);
        
        return publicRooms.map(chatRoomMapper::toChatRoomResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getChatRoomParticipants(String roomId) {
        logger.debug("Getting participants for chat room: {}", roomId);
        
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new ChatRoomNotFoundException("Chat room not found: " + roomId);
        }
        
        return chatRoomRepository.findParticipantsByRoomId(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserInChatRoom(String roomId, Long userId) {
        logger.debug("Checking if user {} is in chat room: {}", userId, roomId);
        
        return chatRoomRepository.isUserParticipant(roomId, userId);
    }

    @Override
    public ChatRoomResponse updateChatRoom(String roomId, Long userId, CreateChatRoomRequest request) {
        logger.info("Updating chat room: {} by user: {}", roomId, userId);
        
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new ChatRoomNotFoundException("Chat room not found: " + roomId);
        }
        
        ChatRoom chatRoom = chatRoomOpt.get();
        
        // Check if user has permission to update (creator or admin)
        if (!chatRoom.getCreatedBy().equals(userId) && !isUserAdmin(chatRoom, userId)) {
            throw new UnauthorizedChatAccessException("User not authorized to update this chat room");
        }
        
        // Update room details
        if (request.getName() != null) {
            chatRoom.setName(request.getName());
        }
        if (request.getDescription() != null) {
            chatRoom.setDescription(request.getDescription());
        }
        if (request.getMaxParticipants() != null) {
            chatRoom.setMaxParticipants(request.getMaxParticipants());
        }
        
        chatRoom.setUpdatedAt(LocalDateTime.now());
        
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        
        // Notify participants of update
        messageBroker.broadcastToRoom(roomId, "ROOM_UPDATED", 
                "Chat room has been updated");
        
        logger.info("Chat room updated: {}", roomId);
        return chatRoomMapper.toChatRoomResponse(savedChatRoom);
    }

    @Override
    public void deleteChatRoom(String roomId, Long userId) {
        logger.info("Deleting chat room: {} by user: {}", roomId, userId);
        
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new ChatRoomNotFoundException("Chat room not found: " + roomId);
        }
        
        ChatRoom chatRoom = chatRoomOpt.get();
        
        // Check if user has permission to delete (creator only)
        if (!chatRoom.getCreatedBy().equals(userId)) {
            throw new UnauthorizedChatAccessException("Only room creator can delete the chat room");
        }
        
        // Notify all participants before deletion
        messageBroker.broadcastToRoom(roomId, "ROOM_DELETED", 
                "Chat room has been deleted");
        
        // Mark as inactive instead of hard delete
        chatRoom.setIsActive(false);
        chatRoom.setUpdatedAt(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
        
        // Remove all participants
        removeAllParticipants(chatRoom.getId());
        
        logger.info("Chat room deleted: {}", roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getChatRoomCount() {
        return chatRoomRepository.countByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveChatRoomCount() {
        LocalDateTime recentActivity = LocalDateTime.now().minusHours(1);
        return chatRoomRepository.countByIsActiveTrueAndLastActivityAtAfter(recentActivity);
    }

    @Override
    public void updateChatRoomActivity(String roomId) {
        logger.debug("Updating activity for chat room: {}", roomId);
        
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(roomId);
        if (chatRoomOpt.isPresent()) {
            ChatRoom chatRoom = chatRoomOpt.get();
            chatRoom.setLastActivityAt(LocalDateTime.now());
            chatRoom.setUpdatedAt(LocalDateTime.now());
            chatRoomRepository.save(chatRoom);
        }
    }

    @Override
    public void cleanupInactiveChatRooms(LocalDateTime cutoffDate) {
        logger.info("Cleaning up inactive chat rooms older than: {}", cutoffDate);
        
        List<ChatRoom> inactiveRooms = chatRoomRepository.findByLastActivityAtBeforeAndIsActiveTrue(cutoffDate);
        
        for (ChatRoom room : inactiveRooms) {
            if (room.getCurrentParticipants() == 0) {
                room.setIsActive(false);
                room.setUpdatedAt(LocalDateTime.now());
                chatRoomRepository.save(room);
                logger.debug("Deactivated inactive chat room: {}", room.getRoomId());
            }
        }
        
        logger.info("Cleaned up {} inactive chat rooms", inactiveRooms.size());
    }

    private String generateRoomId() {
        return "room_" + UUID.randomUUID().toString().replace("-", "");
    }

    private boolean canUserJoinRoom(ChatRoom chatRoom, Long userId) {
        // Public rooms - anyone can join
        if (chatRoom.getType() == ChatRoomType.PUBLIC) {
            return true;
        }
        
        // Private rooms - only invited users
        if (chatRoom.getType() == ChatRoomType.PRIVATE) {
            return isUserInvited(chatRoom, userId);
        }
        
        // Group rooms - invited users or open groups
        if (chatRoom.getType() == ChatRoomType.GROUP) {
            return isUserInvited(chatRoom, userId) || !chatRoom.getIsPrivate();
        }
        
        return false;
    }

    private boolean isUserInvited(ChatRoom chatRoom, Long userId) {
        // TODO: Implement invitation system
        // For now, return true for simplicity
        return true;
    }

    private boolean isUserAdmin(ChatRoom chatRoom, Long userId) {
        // TODO: Implement admin system
        // For now, only creator is admin
        return chatRoom.getCreatedBy().equals(userId);
    }

    private void addParticipant(Long chatRoomId, Long userId) {
        // TODO: Implement participant management
        // This would typically involve a separate participants table
        logger.debug("Adding participant {} to chat room: {}", userId, chatRoomId);
    }

    private void removeParticipant(Long chatRoomId, Long userId) {
        // TODO: Implement participant management
        logger.debug("Removing participant {} from chat room: {}", userId, chatRoomId);
    }

    private void removeAllParticipants(Long chatRoomId) {
        // TODO: Implement participant management
        logger.debug("Removing all participants from chat room: {}", chatRoomId);
    }
}
