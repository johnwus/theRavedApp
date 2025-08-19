package com.raved.realtime.websocket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@org.springframework.context.annotation.Import(com.raved.realtime.config.TestSecurityConfig.class)
class WebSocketIntegrationTest {

    @LocalServerPort
    int port;

    @Test
    void connectToStompEndpoint_succeeds() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());

        String url = "ws://localhost:" + port + "/api/v1/realtime/connect/websocket";
        CompletableFuture<StompSession> future = new CompletableFuture<>();

        stompClient.connect(url, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                future.complete(session);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                future.completeExceptionally(exception);
            }
        });

        StompSession session = future.get(5, TimeUnit.SECONDS);
        assertThat(session.isConnected()).isTrue();
        session.disconnect();
    }
}

