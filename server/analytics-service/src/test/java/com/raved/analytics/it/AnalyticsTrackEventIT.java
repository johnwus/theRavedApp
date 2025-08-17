package com.raved.analytics.it;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.model.EventType;
import com.raved.analytics.repository.AnalyticsEventRepository;
import com.raved.analytics.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MongoDBContainer;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
@ActiveProfiles("local")
@TestPropertySource(properties = "spring.cloud.config.enabled=false")
@EnabledIfEnvironmentVariable(named = "RUN_IT", matches = "true")
class AnalyticsTrackEventIT {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongo.getReplicaSetUrl("raved_analytics"));
    }

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private AnalyticsEventRepository eventRepository;

    @Test
    void trackEvent_persistsToMongo_withComputedFields_andSocialHashtags() {
        TrackEventRequest req = new TrackEventRequest();
        req.setUserId("user-1");
        req.setSessionId("sess-1");
        req.setEventType(EventType.USER_LOGIN);
        req.setEventTimestamp(LocalDateTime.of(2025, 8, 15, 10, 30));
        TrackEventRequest.SocialDataRequest social = new TrackEventRequest.SocialDataRequest();
        social.setPostId("p1");
        social.setHashtags(List.of("raved", "fun"));
        req.setSocialData(social);

        var resp = analyticsService.trackEvent(req);
        assertThat(resp.getId()).isNotNull();

        var all = eventRepository.findAll();
        assertThat(all).isNotEmpty();
        var e = all.get(0);
        assertThat(e.getEventDate()).isEqualTo("2025-08-15");
        assertThat(e.getEventHour()).isEqualTo(10);
        assertThat(e.getEventMonth()).isEqualTo(8);
        assertThat(e.getEventWeekday()).isBetween(1, 7);
        assertThat(e.getSocialData()).isNotNull();
        assertThat(e.getSocialData().getHashtags()).contains("raved").contains("fun");
    }
}
