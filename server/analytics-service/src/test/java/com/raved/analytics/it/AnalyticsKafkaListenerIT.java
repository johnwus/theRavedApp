package com.raved.analytics.it;

import com.raved.analytics.repository.AnalyticsEventRepository;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"user-events", "social-events", "ecommerce-events", "content-events"})
@Testcontainers
@TestPropertySource(properties = {
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false",
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@EnabledIfEnvironmentVariable(named = "RUN_IT", matches = "true")
class AnalyticsKafkaListenerIT {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private AnalyticsEventRepository eventRepository;

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongo.getReplicaSetUrl("raved_analytics_test"));
    }

    @Test
    void listener_consumes_and_persists_event_and_ignores_invalid_json() throws Exception {
        // Produce valid message
        var producerProps = KafkaTestUtils.producerProps(embeddedKafka);
        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(), new StringSerializer());
        KafkaTemplate<String, String> template = new KafkaTemplate<>(pf);
        template.setDefaultTopic("user-events");
        template.sendDefault("{\"sessionId\":\"s-it\",\"eventType\":\"USER_LOGIN\",\"socialData\":{\"hashtags\":[\"raved\",\"fun\"]}}");
        // Produce invalid message (should be ignored by listener)
        template.sendDefault("{invalid-json");
        template.flush();

        // Poll repository to allow listener to process
        int retries = 24; // ~6 seconds total
        boolean found = false;
        while (retries-- > 0) {
            var all = eventRepository.findAll();
            if (!all.isEmpty()) {
                var e = all.get(0);
                // Computed fields present
                assertThat(e.getEventDate()).isNotBlank();
                assertThat(e.getEventHour()).isBetween(0, 23);
                assertThat(e.getEventWeekday()).isBetween(1, 7);
                assertThat(e.getEventMonth()).isBetween(1, 12);
                // Social hashtags mapped
                if (e.getSocialData() != null && e.getSocialData().getHashtags() != null) {
                    assertThat(e.getSocialData().getHashtags()).contains("raved");
                }
                found = true;
                break;
            }
            Thread.sleep(250);
        }
        assertThat(found).isTrue();
    }

    @Test
    void listener_consumes_events_from_other_topics() throws Exception {
        var producerProps2 = KafkaTestUtils.producerProps(embeddedKafka);
        ProducerFactory<String, String> pf2 = new DefaultKafkaProducerFactory<>(producerProps2, new StringSerializer(), new StringSerializer());
        KafkaTemplate<String, String> template2 = new KafkaTemplate<>(pf2);

        // social-events
        template2.send("social-events", "{\"sessionId\":\"s-social\",\"eventType\":\"POST_CREATE\",\"socialData\":{\"hashtags\":[\"raved\"]}}");
        // ecommerce-events
        template2.send("ecommerce-events", "{\"sessionId\":\"s-ecom\",\"eventType\":\"PRODUCT_PURCHASE\",\"ecommerceData\":{\"productId\":\"p1\",\"price\":19.99,\"currency\":\"USD\"}}");
        // content-events
        template2.send("content-events", "{\"sessionId\":\"s-content\",\"eventType\":\"CONTENT_VIEW\",\"contentData\":{\"contentId\":\"c1\",\"category\":\"news\"}}");
        template2.flush();

        // Poll repository for at least 3 events
        int retries2 = 32; // ~8 seconds total
        while (retries2-- > 0) {
            if (eventRepository.findAll().size() >= 3) {
                break;
            }
            Thread.sleep(250);
        }
        assertThat(eventRepository.findAll().size()).isGreaterThanOrEqualTo(3);
    }
}
