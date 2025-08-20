package com.raved.notification.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raved.notification.model.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.junit.jupiter.api.Disabled("Temporarily disabled due to Mockito inline agent limitations on JDK 24; switch to stub or adjust producer to KafkaOperations for easier testing.")
class NotificationProducerTest {

    @Mock
    KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    NotificationProducer producer;

    @Test
    void sendNotificationEvent_sendsToTopic() throws Exception {
        Notification n = new Notification();
        n.setId("123");
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(java.util.concurrent.CompletableFuture.completedFuture(null));
        producer.sendNotificationEvent(n);
        verify(kafkaTemplate).send(eq("notification-events"), eq("123"), anyString());
    }
}

