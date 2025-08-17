package com.raved.realtime.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ChatWebSocketHandler {

    @Autowired
    private MessageBroker messageBroker;

    @MessageMapping("/chat/rooms/{roomId}/send")
    public void sendToRoom(@DestinationVariable String roomId, String payload) {
        messageBroker.broadcastToRoom(roomId, "NEW_MESSAGE", payload);
    }

    @MessageMapping("/typing/start")
    public void typingStart(Map<String, Object> payload) {
        Object roomId = payload.get("roomId");
        if (roomId != null) {
            messageBroker.broadcastToRoom(roomId.toString(), "TYPING_START", payload);
        }
    }

    @MessageMapping("/typing/stop")
    public void typingStop(Map<String, Object> payload) {
        Object roomId = payload.get("roomId");
        if (roomId != null) {
            messageBroker.broadcastToRoom(roomId.toString(), "TYPING_STOP", payload);
        }
    }
}
