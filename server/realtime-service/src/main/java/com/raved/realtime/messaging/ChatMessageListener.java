package com.raved.realtime.messaging;

import com.raved.realtime.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageListener.class);

    @RabbitListener(queues = RabbitMQConfig.CHAT_QUEUE)
    public void onChatMessage(@Payload String message) {
        logger.info("Received chat message: {}", message);
        // TODO: route to WebSocket broadcasts
    }
}

