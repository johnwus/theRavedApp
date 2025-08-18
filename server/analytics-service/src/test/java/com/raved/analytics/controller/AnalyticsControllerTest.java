package com.raved.analytics.controller;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.dto.response.AnalyticsEventResponse;
import com.raved.analytics.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AnalyticsController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @Test
    void trackEvent_shouldReturn201() throws Exception {
        AnalyticsEventResponse resp = new AnalyticsEventResponse();
        resp.setId("e1");
        Mockito.when(analyticsService.trackEvent(Mockito.any(TrackEventRequest.class))).thenReturn(resp);

        String body = "{"
                + "\"sessionId\":\"s1\","
                + "\"eventType\":\"USER_LOGIN\""
                + "}";

        mockMvc.perform(post("/api/analytics/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());

    }

    @Test
    void getTotalEventCount_shouldReturn200() throws Exception {
        Mockito.when(analyticsService.getTotalEventCount()).thenReturn(5L);
        mockMvc.perform(get("/api/analytics/events/counts/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
