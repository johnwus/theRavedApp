package com.raved.realtime.service.impl;

import com.raved.realtime.service.WebSocketService;
import com.raved.realtime.websocket.MessageBroker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private MessageBroker messageBroker;

    @Override
    public void sendToRoom(String roomId, String event, String payload) {
        messageBroker.broadcastToRoom(roomId, event, payload);
    }
}

