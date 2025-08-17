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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"user-events"})
@Testcontainers
@TestPropertySource(properties = {
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false",
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@EnabledIfEnvironmentVariable(named = "RUN_IT", matches = "true")
class AnalyticsKafkaIdempotenceIT {

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

    private KafkaTemplate<String, String> template() {
        var producerProps = KafkaTestUtils.producerProps(embeddedKafka);
        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(), new StringSerializer());
        KafkaTemplate<String, String> t = new KafkaTemplate<>(pf);
        t.setDefaultTopic("user-events");
        return t;
    }

    @Test
    void duplicate_messages_with_same_dedupKey_persist_only_once() throws Exception {
        KafkaTemplate<String, String> t = template();
        String sessionId = "s-idem";
        String eventType = "USER_LOGIN";
        String entityId = ""; // none
        // Fixed timestamp (second precision)
        LocalDateTime ts = LocalDateTime.of(2025, 1, 10, 12, 34, 56);
        String tsIso = ts.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String json = "{" +
                "\"sessionId\":\"" + sessionId + "\"," +
                "\"eventType\":\"" + eventType + "\"," +
                "\"eventTimestamp\":\"" + tsIso + "\"" +
                "}";

        // Send same message twice
        t.sendDefault(json);
        t.sendDefault(json);
        t.flush();

        // Poll repository to allow listener to process
        int retries = 32; // ~8 seconds total
        boolean ok = false;
        while (retries-- > 0) {
            var list = eventRepository.findBySessionId(sessionId);
            long countAtSecond = list.stream()
                    .filter(e -> e.getEventType() != null && e.getEventType().name().equals(eventType))
                    .filter(e -> e.getEventTimestamp() != null && e.getEventTimestamp().withNano(0).equals(ts))
                    .count();
            if (countAtSecond >= 1) {
                assertThat(countAtSecond).isEqualTo(1L);
                ok = true;
                break;
            }
            Thread.sleep(250);
        }
        assertThat(ok).isTrue();
    }

    @Test
    void concurrent_duplicates_still_result_in_single_persist() throws Exception {
        KafkaTemplate<String, String> t = template();
        String sessionId = "s-idem-concurrent";
        String eventType = "USER_LOGIN";
        LocalDateTime ts = LocalDateTime.of(2025, 1, 10, 13, 0, 5);
        String tsIso = ts.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String json = "{" +
                "\"sessionId\":\"" + sessionId + "\"," +
                "\"eventType\":\"" + eventType + "\"," +
                "\"eventTimestamp\":\"" + tsIso + "\"" +
                "}";

        int N = 5;
        var pool = Executors.newFixedThreadPool(N);
        CountDownLatch latch = new CountDownLatch(N);
        for (int i = 0; i < N; i++) {
            pool.submit(() -> { t.sendDefault(json); latch.countDown(); });
        }
        latch.await(5, TimeUnit.SECONDS);
        t.flush();

        int retries = 40;
        boolean ok = false;
        while (retries-- > 0) {
            var list = eventRepository.findBySessionId(sessionId);
            long countAtSecond = list.stream()
                    .filter(e -> e.getEventType() != null && e.getEventType().name().equals(eventType))
                    .filter(e -> e.getEventTimestamp() != null && e.getEventTimestamp().withNano(0).equals(ts))
                    .count();
            if (countAtSecond >= 1) {
                assertThat(countAtSecond).isEqualTo(1L);
                ok = true;
                break;
            }
            Thread.sleep(250);
        }
        assertThat(ok).isTrue();
    }
}

