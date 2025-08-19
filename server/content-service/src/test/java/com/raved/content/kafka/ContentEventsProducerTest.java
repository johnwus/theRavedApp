package com.raved.content.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raved.content.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentEventsProducerTest {

    @Mock
    KafkaOperations<String, String> kafkaOperations;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    ContentEventsProducer producer;

    @BeforeEach
    void setup() {
        // Ensure the default topic value is set for the test
        ReflectionTestUtils.setField(producer, "contentCreatedTopic", "content.created");
    }

    @Test
    void publishPostCreated_sendsKafkaMessage() throws Exception {
        // Arrange
        Post post = new Post();
        post.setId("post-123");
        post.setUserId("user-456");

        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        when(kafkaOperations.send(anyString(), anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(null));

        // Act
        producer.publishPostCreated(post);

        // Assert
        verify(kafkaOperations, times(1))
                .send(eq("content.created"), eq("post-123"), anyString());
    }
}

