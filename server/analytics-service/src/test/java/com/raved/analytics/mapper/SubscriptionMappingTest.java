package com.raved.analytics.mapper;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.EventType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SubscriptionMappingTest {

    private final AnalyticsEventMapper mapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Test
    void subscriptionData_isMapped_withComputedFields() {
        TrackEventRequest req = new TrackEventRequest();
        req.setSessionId("s-sub");
        req.setEventType(EventType.SUBSCRIPTION_START);
        var sub = new TrackEventRequest.SubscriptionDataRequest();
        sub.setSubscriptionId("sub1");
        sub.setPlanId("pro");
        sub.setPlanName("Pro");
        sub.setPlanType("MONTHLY");
        sub.setPlanPrice(new BigDecimal("9.99"));
        sub.setCurrency("USD");
        req.setSubscriptionData(sub);

        AnalyticsEvent entity = mapper.toEntity(req);
        assertThat(entity.getSubscriptionData()).isNotNull();
        assertThat(entity.getSubscriptionData().getSubscriptionId()).isEqualTo("sub1");
        assertThat(entity.getSubscriptionData().getPlanPrice()).isEqualTo(9.99);
        assertThat(entity.getEventDate()).isNotBlank();
        assertThat(entity.getEventHour()).isBetween(0, 23);
        assertThat(entity.getEventWeekday()).isBetween(1, 7);
        assertThat(entity.getEventMonth()).isBetween(1, 12);
    }
}

