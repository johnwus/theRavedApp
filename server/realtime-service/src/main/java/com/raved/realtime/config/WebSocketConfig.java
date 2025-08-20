package com.raved.realtime.config;

import com.raved.realtime.websocket.WebSocketChannelInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Support both hyphen and dot property names for env var mapping flexibility
    @Value("${websocket.allowed-origins:${websocket.allowed.origins:*}}")
    private String allowedOrigins;

    @Value("${websocket.interceptor.enabled:true}")
    private boolean interceptorEnabled;

    @Value("${websocket.sockjs.enabled:true}")
    private boolean sockJsEnabled;

    @Autowired
    @org.springframework.context.annotation.Lazy
    private WebSocketChannelInterceptor webSocketChannelInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Direct WebSocket endpoint
        registry.addEndpoint("/api/v1/realtime/connect")
                .setAllowedOriginPatterns(allowedOrigins);
        // SockJS fallback endpoint
        registry.addEndpoint("/api/v1/realtime/connect")
                .setAllowedOriginPatterns(allowedOrigins)
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        if (interceptorEnabled) {
            registration.interceptors(webSocketChannelInterceptor);
        }
    }
}
