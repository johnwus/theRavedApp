package com.raved.realtime.websocket;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class ChatWebSocketHandlerTest {

    @Test
    void sendToRoom_broadcastsNewMessage() {
        MessageBroker broker = Mockito.mock(MessageBroker.class);
        ChatWebSocketHandler handler = new ChatWebSocketHandler();
        try {
            var field = ChatWebSocketHandler.class.getDeclaredField("messageBroker");
            field.setAccessible(true);
            field.set(handler, broker);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        handler.sendToRoom("room-42", "hello");
        verify(broker).broadcastToRoom(eq("room-42"), eq("NEW_MESSAGE"), eq("hello"));
    }
}

