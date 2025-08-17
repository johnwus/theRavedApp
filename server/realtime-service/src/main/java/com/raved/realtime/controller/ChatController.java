package com.raved.realtime.controller;

import com.raved.realtime.dto.request.CreateChatRoomRequest;
import com.raved.realtime.dto.request.JoinChatRoomRequest;
import com.raved.realtime.dto.response.ChatRoomResponse;
import com.raved.realtime.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomResponse> createRoom(@Valid @RequestBody CreateChatRoomRequest request) {
        return ResponseEntity.ok(chatService.createChatRoom(request));
    }

    @PostMapping("/rooms/join")
    public ResponseEntity<ChatRoomResponse> joinRoom(@Valid @RequestBody JoinChatRoomRequest request) {
        return ResponseEntity.ok(chatService.joinChatRoom(request));
    }

    @PostMapping("/rooms/{roomId}/leave")
    public ResponseEntity<Void> leaveRoom(@PathVariable String roomId, @RequestParam Long userId) {
        chatService.leaveChatRoom(roomId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ChatRoomResponse> getRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getChatRoom(roomId));
    }

    @GetMapping("/rooms/user/{userId}")
    public ResponseEntity<Page<ChatRoomResponse>> getUserRooms(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(chatService.getUserChatRooms(userId, pageable));
    }

    @GetMapping("/rooms/public")
    public ResponseEntity<Page<ChatRoomResponse>> getPublicRooms(Pageable pageable) {
        return ResponseEntity.ok(chatService.getPublicChatRooms(pageable));
    }
}
