package com.raved.analytics.repository;

import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@EnabledIfEnvironmentVariable(named = "RUN_IT", matches = "true")
class AnalyticsEventRepositoryIT {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongo.getReplicaSetUrl("raved_analytics_test"));
    }

    @Autowired
    private AnalyticsEventRepository repo;

    @Test
    void findByUserId_and_findByEventType_work() {
        AnalyticsEvent e1 = new AnalyticsEvent();
        e1.setUserId("u1");
        e1.setEventType(EventType.USER_LOGIN);
        e1.setEventTimestamp(LocalDateTime.now().minusHours(1));

        AnalyticsEvent e2 = new AnalyticsEvent();
        e2.setUserId("u2");
        e2.setEventType(EventType.POST_VIEW);
        e2.setEventTimestamp(LocalDateTime.now());

        repo.saveAll(List.of(e1, e2));

        assertThat(repo.findByUserId("u1")).hasSize(1);
        assertThat(repo.findByEventType(EventType.POST_VIEW)).hasSize(1);
    }

    @Test
    void countByEventTypeAndEventTimestampBetween_and_findPostsByHashtag_work() {
        AnalyticsEvent e = new AnalyticsEvent();
        e.setUserId("u3");
        e.setEventType(EventType.POST_CREATE);
        e.setEventTimestamp(LocalDateTime.now());
        AnalyticsEvent.SocialData social = new AnalyticsEvent.SocialData();
        social.setHashtags("raved,fun");
        e.setSocialData(social);
        repo.save(e);

        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        long count = repo.countByEventTypeAndEventTimestampBetween(EventType.POST_CREATE, start, end);
        assertThat(count).isGreaterThanOrEqualTo(1);

        assertThat(repo.findPostsByHashtag("raved")).isNotEmpty();
    }
}

