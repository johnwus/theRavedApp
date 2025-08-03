package com.raved.realtime.service.impl;

import com.raved.realtime.dto.request.SendMessageRequest;
import com.raved.realtime.dto.response.MessageResponse;
import com.raved.realtime.exception.ChatRoomNotFoundException;
import com.raved.realtime.exception.MessageNotFoundException;
import com.raved.realtime.exception.UnauthorizedChatAccessException;
import com.raved.realtime.mapper.MessageMapper;
import com.raved.realtime.model.ChatRoom;
import com.raved.realtime.model.Message;
import com.raved.realtime.model.MessageReaction;
import com.raved.realtime.model.MessageStatus;
import com.raved.realtime.model.MessageType;
import com.raved.realtime.repository.ChatRoomRepository;
import com.raved.realtime.repository.MessageReactionRepository;
import com.raved.realtime.repository.MessageRepository;
import com.raved.realtime.service.ChatService;
import com.raved.realtime.service.MessageService;
import com.raved.realtime.websocket.MessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of MessageService
 */
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageReactionRepository reactionRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageBroker messageBroker;

    @Override
    public MessageResponse sendMessage(SendMessageRequest request) {
        logger.info("Sending message from user {} to room {}", request.getSenderId(), request.getRoomId());
        
        // Validate chat room exists and user has access
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(request.getRoomId());
        if (chatRoomOpt.isEmpty()) {
            throw new ChatRoomNotFoundException("Chat room not found: " + request.getRoomId());
        }
        
        ChatRoom chatRoom = chatRoomOpt.get();
        if (!chatService.isUserInChatRoom(request.getRoomId(), request.getSenderId())) {
            throw new UnauthorizedChatAccessException("User not authorized to send messages to this room");
        }
        
        // Create message
        Message message = messageMapper.toMessage(request);
        message.setMessageId(generateMessageId());
        message.setChatRoomId(chatRoom.getId());
        message.setStatus(MessageStatus.SENT);
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        
        Message savedMessage = messageRepository.save(message);
        
        // Update chat room activity
        chatService.updateChatRoomActivity(request.getRoomId());
        
        // Broadcast message to room participants
        MessageResponse messageResponse = messageMapper.toMessageResponse(savedMessage);
        messageBroker.broadcastToRoom(request.getRoomId(), "NEW_MESSAGE", messageResponse);
        
        // Mark message as delivered
        markMessageAsDelivered(savedMessage.getId());
        
        logger.info("Message sent: {}", savedMessage.getId());
        return messageResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public MessageResponse getMessage(Long messageId) {
        logger.debug("Getting message: {}", messageId);
        
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new MessageNotFoundException("Message not found: " + messageId);
        }
        
        return messageMapper.toMessageResponse(messageOpt.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageResponse> getChatRoomMessages(String roomId, Long userId, Pageable pageable) {
        logger.debug("Getting messages for room {} by user {}", roomId, userId);
        
        // Validate user has access to room
        if (!chatService.isUserInChatRoom(roomId, userId)) {
            throw new UnauthorizedChatAccessException("User not authorized to view messages in this room");
        }
        
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new ChatRoomNotFoundException("Chat room not found: " + roomId);
        }
        
        Long chatRoomId = chatRoomOpt.get().getId();
        Page<Message> messages = messageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable);
        
        return messages.map(messageMapper::toMessageResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageResponse> getMessagesByUser(Long userId, Pageable pageable) {
        logger.debug("Getting messages by user: {}", userId);
        
        Page<Message> messages = messageRepository.findBySenderIdOrderByCreatedAtDesc(userId, pageable);
        return messages.map(messageMapper::toMessageResponse);
    }

    @Override
    public MessageResponse editMessage(Long messageId, Long userId, String newContent) {
        logger.info("Editing message {} by user {}", messageId, userId);
        
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new MessageNotFoundException("Message not found: " + messageId);
        }
        
        Message message = messageOpt.get();
        
        // Check if user is the sender
        if (!message.getSenderId().equals(userId)) {
            throw new UnauthorizedChatAccessException("User not authorized to edit this message");
        }
        
        // Check if message can be edited (within time limit)
        LocalDateTime editCutoff = message.getCreatedAt().plusMinutes(15); // 15 minutes edit window
        if (LocalDateTime.now().isAfter(editCutoff)) {
            throw new UnauthorizedChatAccessException("Message edit time limit exceeded");
        }
        
        message.setContent(newContent);
        message.setIsEdited(true);
        message.setUpdatedAt(LocalDateTime.now());
        
        Message savedMessage = messageRepository.save(message);
        
        // Broadcast message update
        MessageResponse messageResponse = messageMapper.toMessageResponse(savedMessage);
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(message.getChatRoomId());
        if (chatRoomOpt.isPresent()) {
            messageBroker.broadcastToRoom(chatRoomOpt.get().getRoomId(), "MESSAGE_EDITED", messageResponse);
        }
        
        logger.info("Message edited: {}", messageId);
        return messageResponse;
    }

    @Override
    public void deleteMessage(Long messageId, Long userId) {
        logger.info("Deleting message {} by user {}", messageId, userId);
        
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new MessageNotFoundException("Message not found: " + messageId);
        }
        
        Message message = messageOpt.get();
        
        // Check if user is the sender
        if (!message.getSenderId().equals(userId)) {
            throw new UnauthorizedChatAccessException("User not authorized to delete this message");
        }
        
        // Soft delete - mark as deleted instead of removing
        message.setIsDeleted(true);
        message.setContent("[Message deleted]");
        message.setUpdatedAt(LocalDateTime.now());
        
        messageRepository.save(message);
        
        // Broadcast message deletion
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(message.getChatRoomId());
        if (chatRoomOpt.isPresent()) {
            Map<String, Object> deleteEvent = new HashMap<>();
            deleteEvent.put("messageId", messageId);
            deleteEvent.put("deletedBy", userId);
            messageBroker.broadcastToRoom(chatRoomOpt.get().getRoomId(), "MESSAGE_DELETED", deleteEvent);
        }
        
        logger.info("Message deleted: {}", messageId);
    }

    @Override
    public void markMessageAsRead(Long messageId, Long userId) {
        logger.debug("Marking message {} as read by user {}", messageId, userId);
        
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            
            // Don't mark own messages as read
            if (!message.getSenderId().equals(userId)) {
                message.setStatus(MessageStatus.READ);
                message.setReadAt(LocalDateTime.now());
                message.setUpdatedAt(LocalDateTime.now());
                messageRepository.save(message);
                
                // Notify sender that message was read
                Map<String, Object> readEvent = new HashMap<>();
                readEvent.put("messageId", messageId);
                readEvent.put("readBy", userId);
                readEvent.put("readAt", LocalDateTime.now());
                
                Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(message.getChatRoomId());
                if (chatRoomOpt.isPresent()) {
                    messageBroker.sendToUser(message.getSenderId(), "MESSAGE_READ", readEvent);
                }
            }
        }
    }

    @Override
    public void markMessagesAsRead(String roomId, Long userId) {
        logger.debug("Marking all messages as read in room {} by user {}", roomId, userId);
        
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(roomId);
        if (chatRoomOpt.isPresent()) {
            Long chatRoomId = chatRoomOpt.get().getId();
            
            List<Message> unreadMessages = messageRepository.findByChatRoomIdAndStatusAndSenderIdNot(
                    chatRoomId, MessageStatus.SENT, userId);
            
            LocalDateTime now = LocalDateTime.now();
            for (Message message : unreadMessages) {
                message.setStatus(MessageStatus.READ);
                message.setReadAt(now);
                message.setUpdatedAt(now);
            }
            
            messageRepository.saveAll(unreadMessages);
            
            logger.debug("Marked {} messages as read", unreadMessages.size());
        }
    }

    @Override
    public void addReaction(Long messageId, Long userId, String emoji) {
        logger.info("Adding reaction {} to message {} by user {}", emoji, messageId, userId);
        
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isEmpty()) {
            throw new MessageNotFoundException("Message not found: " + messageId);
        }
        
        Message message = messageOpt.get();
        
        // Check if user already reacted with this emoji
        Optional<MessageReaction> existingReaction = reactionRepository.findByMessageIdAndUserIdAndEmoji(
                messageId, userId, emoji);
        
        if (existingReaction.isPresent()) {
            logger.debug("User {} already reacted with {} to message {}", userId, emoji, messageId);
            return;
        }
        
        // Create new reaction
        MessageReaction reaction = new MessageReaction();
        reaction.setMessageId(messageId);
        reaction.setUserId(userId);
        reaction.setEmoji(emoji);
        reaction.setCreatedAt(LocalDateTime.now());
        
        reactionRepository.save(reaction);
        
        // Broadcast reaction
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(message.getChatRoomId());
        if (chatRoomOpt.isPresent()) {
            Map<String, Object> reactionEvent = new HashMap<>();
            reactionEvent.put("messageId", messageId);
            reactionEvent.put("userId", userId);
            reactionEvent.put("emoji", emoji);
            reactionEvent.put("action", "ADD");
            
            messageBroker.broadcastToRoom(chatRoomOpt.get().getRoomId(), "MESSAGE_REACTION", reactionEvent);
        }
        
        logger.info("Reaction added: {} to message {}", emoji, messageId);
    }

    @Override
    public void removeReaction(Long messageId, Long userId, String emoji) {
        logger.info("Removing reaction {} from message {} by user {}", emoji, messageId, userId);
        
        Optional<MessageReaction> reactionOpt = reactionRepository.findByMessageIdAndUserIdAndEmoji(
                messageId, userId, emoji);
        
        if (reactionOpt.isPresent()) {
            reactionRepository.delete(reactionOpt.get());
            
            // Broadcast reaction removal
            Optional<Message> messageOpt = messageRepository.findById(messageId);
            if (messageOpt.isPresent()) {
                Message message = messageOpt.get();
                Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(message.getChatRoomId());
                
                if (chatRoomOpt.isPresent()) {
                    Map<String, Object> reactionEvent = new HashMap<>();
                    reactionEvent.put("messageId", messageId);
                    reactionEvent.put("userId", userId);
                    reactionEvent.put("emoji", emoji);
                    reactionEvent.put("action", "REMOVE");
                    
                    messageBroker.broadcastToRoom(chatRoomOpt.get().getRoomId(), "MESSAGE_REACTION", reactionEvent);
                }
            }
            
            logger.info("Reaction removed: {} from message {}", emoji, messageId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadMessageCount(String roomId, Long userId) {
        logger.debug("Getting unread message count for room {} and user {}", roomId, userId);
        
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(roomId);
        if (chatRoomOpt.isEmpty()) {
            return 0;
        }
        
        Long chatRoomId = chatRoomOpt.get().getId();
        return messageRepository.countByChatRoomIdAndStatusAndSenderIdNot(
                chatRoomId, MessageStatus.SENT, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalMessageCount() {
        return messageRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getMessageCountForRoom(String roomId) {
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByRoomId(roomId);
        if (chatRoomOpt.isEmpty()) {
            return 0;
        }
        
        return messageRepository.countByChatRoomId(chatRoomOpt.get().getId());
    }

    @Override
    public void cleanupOldMessages(LocalDateTime cutoffDate) {
        logger.info("Cleaning up messages older than: {}", cutoffDate);
        
        long deletedCount = messageRepository.deleteByCreatedAtBefore(cutoffDate);
        logger.info("Cleaned up {} old messages", deletedCount);
    }

    private void markMessageAsDelivered(Long messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            message.setStatus(MessageStatus.DELIVERED);
            message.setDeliveredAt(LocalDateTime.now());
            message.setUpdatedAt(LocalDateTime.now());
            messageRepository.save(message);
        }
    }

    private String generateMessageId() {
        return "msg_" + UUID.randomUUID().toString().replace("-", "");
    }
}
