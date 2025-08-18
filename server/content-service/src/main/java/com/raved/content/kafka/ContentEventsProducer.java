package com.raved.content.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raved.content.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ContentEventsProducer {

    private static final Logger logger = LoggerFactory.getLogger(ContentEventsProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${kafka.topics.content-created:content.created}")
    private String contentCreatedTopic;

    public void publishPostCreated(Post post) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "CONTENT_CREATED");
            event.put("postId", post.getId());
            event.put("userId", post.getUserId());
            event.put("timestamp", System.currentTimeMillis());

            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(contentCreatedTopic, post.getId(), payload)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Published content created event: {} to topic: {}", post.getId(), contentCreatedTopic);
                    } else {
                        logger.error("Failed to publish content created event: {}", post.getId(), ex);
                    }
                });
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize content created event for post: {}", post.getId(), e);
        }
    }
}

