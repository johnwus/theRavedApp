package com.raved.realtime.messaging;

import com.raved.realtime.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PresenceListener {

    private static final Logger logger = LoggerFactory.getLogger(PresenceListener.class);

    @RabbitListener(queues = RabbitMQConfig.PRESENCE_QUEUE)
    public void onPresenceUpdate(@Payload String payload) {
        logger.debug("Presence update: {}", payload);
        // TODO: update presence cache and broadcast
    }
}

