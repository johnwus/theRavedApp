package com.raved.analytics.service;

import com.raved.analytics.dto.response.AnalyticsEventResponse;
import com.raved.analytics.service.impl.AnalyticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AnalyticsServiceKafkaParsingTest {

    @Test
    void parseEventMessage_and_createTrackEventRequest_shouldWork() throws Exception {
        AnalyticsServiceImpl impl = new AnalyticsServiceImpl();

        String json = "{\"sessionId\":\"s1\",\"eventType\":\"USER_LOGIN\"}";
        Map<String, Object> data = (Map<String, Object>) ReflectionTestUtils.invokeMethod(impl, "parseEventMessage", json);
        assertThat(data).isNotNull();

        Object req = ReflectionTestUtils.invokeMethod(impl, "createTrackEventRequest", data);
        assertThat(req).isNotNull();
    }
}
