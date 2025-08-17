package com.raved.analytics.mapper;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.EventType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsEventMapperTest {

    private final AnalyticsEventMapper mapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Test
    void toEntity_shouldPopulateComputedFields_andNestedData() {
        TrackEventRequest req = new TrackEventRequest();
        req.setUserId("u1");
        req.setSessionId("s1");
        req.setEventType(EventType.USER_LOGIN);
        req.setEventTimestamp(LocalDateTime.of(2025, 8, 15, 10, 30));

        TrackEventRequest.SocialDataRequest social = new TrackEventRequest.SocialDataRequest();
        social.setPostId("p1");
        social.setHashtags(List.of("raved", "fun"));
        req.setSocialData(social);

        AnalyticsEvent entity = mapper.toEntity(req);
        assertThat(entity.getEventDate()).isEqualTo("2025-08-15");
        assertThat(entity.getEventHour()).isEqualTo(10);
        assertThat(entity.getEventWeekday()).isBetween(1, 7);
        assertThat(entity.getEventMonth()).isEqualTo(8);
        assertThat(entity.getSocialData()).isNotNull();
        assertThat(entity.getSocialData().getHashtags()).isEqualTo("raved,fun");
    }
}

