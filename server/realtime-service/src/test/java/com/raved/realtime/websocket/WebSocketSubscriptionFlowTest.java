package com.raved.realtime.websocket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@org.springframework.test.context.TestPropertySource(properties = {
    "websocket.membership.check=false",
    "websocket.auth.required=false"
})
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(com.raved.realtime.config.TestSecurityConfig.class)
class WebSocketSubscriptionFlowTest {

    @LocalServerPort
    int port;

    @Test
    void sendMessage_reachesSubscribedTopic() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = "ws://localhost:" + port + "/api/v1/realtime/connect";
        StompSession session = stompClient.connect(url, new StompSessionHandlerAdapter() {}).get(5, TimeUnit.SECONDS);

        ArrayBlockingQueue<Map> queue = new ArrayBlockingQueue<>(1);

        session.subscribe("/topic/room/room-99", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) { return Map.class; }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                queue.offer((Map) payload);
            }
        });

        session.send("/app/chat/rooms/room-99/send", "hello world");

        Map message = queue.poll(5, TimeUnit.SECONDS);
        assertThat(message).isNotNull();
        assertThat(message.get("eventType")).isEqualTo("NEW_MESSAGE");
        assertThat(message.get("payload")).isEqualTo("hello world");

        session.disconnect();
    }
}

