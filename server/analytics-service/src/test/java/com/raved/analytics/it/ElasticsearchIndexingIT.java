package com.raved.analytics.it;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.model.EventType;
import com.raved.analytics.search.AnalyticsEventSearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@EnabledIfEnvironmentVariable(named = "RUN_IT", matches = "true")
class ElasticsearchIndexingIT {

    @Container
    static ElasticsearchContainer es = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.11.1")
            .withEnv("xpack.security.enabled", "false")
            .withStartupTimeout(Duration.ofSeconds(120));

    @DynamicPropertySource
    static void esProps(DynamicPropertyRegistry r) {
        r.add("spring.elasticsearch.uris", es::getHttpHostAddress);
        r.add("spring.elasticsearch.username", () -> "");
        r.add("spring.elasticsearch.password", () -> "");
    }

    @Autowired
    private AnalyticsEventSearchRepository searchRepo;

    @Autowired
    private com.raved.analytics.service.AnalyticsService analyticsService;

    @Test
    void trackEvent_indexesDocumentInElasticsearch() throws Exception {
        // Track a social event with hashtag to ensure mapping
        TrackEventRequest req = new TrackEventRequest();
        req.setSessionId("s-es");
        req.setEventType(EventType.POST_CREATE);
        TrackEventRequest.SocialDataRequest social = new TrackEventRequest.SocialDataRequest();
        social.setHashtags(java.util.List.of("raved"));
        req.setSocialData(social);

        var resp = analyticsService.trackEvent(req);
        assertThat(resp.getId()).isNotNull();

        // allow async index
        int retries = 24;
        while (retries-- > 0) {
            boolean exists = searchRepo.existsById(resp.getId());
            if (exists) {
                var doc = searchRepo.findById(resp.getId()).orElseThrow();
                assertThat(doc.getHashtags()).contains("raved");
                assertThat(doc.getEventDate()).isNotBlank();
                break;
            }
            TimeUnit.MILLISECONDS.sleep(250);
        }
        assertThat(searchRepo.existsById(resp.getId())).isTrue();
    }
}

