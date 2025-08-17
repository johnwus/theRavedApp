package com.raved.realtime.websocket;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class MessageBrokerTest {

    @Test
    void broadcastToRoom_sendsToTopic() {
        SimpMessagingTemplate template = Mockito.mock(SimpMessagingTemplate.class);
        MessageBroker broker = new MessageBroker();
        // inject mock
        try {
            var field = MessageBroker.class.getDeclaredField("messagingTemplate");
            field.setAccessible(true);
            field.set(broker, template);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        broker.broadcastToRoom("room-1", "TEST", "hello");

        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);
        verify(template).convertAndSend(eq("/topic/room/room-1"), payloadCaptor.capture());
        Object payload = payloadCaptor.getValue();
        assertThat(payload).isInstanceOf(java.util.Map.class);
        assertThat(((java.util.Map<?, ?>) payload).get("eventType")).isEqualTo("TEST");
        assertThat(((java.util.Map<?, ?>) payload).get("payload")).isEqualTo("hello");
    }
}

