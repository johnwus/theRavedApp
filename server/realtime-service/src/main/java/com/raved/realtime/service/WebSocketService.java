package com.raved.realtime.service;

/**
 * WebSocketService for TheRavedApp
 */
public interface WebSocketService {
    void sendToRoom(String roomId, String event, String payload);
}
