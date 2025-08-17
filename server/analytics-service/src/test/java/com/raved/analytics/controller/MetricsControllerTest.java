package com.raved.analytics.controller;

import com.raved.analytics.dto.response.ContentMetricsResponse;
import com.raved.analytics.dto.response.UserMetricsResponse;
import com.raved.analytics.service.MetricsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MetricsController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class MetricsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricsService metricsService;

    @Test
    void getUserMetrics_shouldReturn200() throws Exception {
        Mockito.when(metricsService.getUserMetrics("u1")).thenReturn(new UserMetricsResponse());
        mockMvc.perform(get("/api/analytics/metrics/users/u1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getContentMetrics_shouldReturn200() throws Exception {
        Mockito.when(metricsService.getContentMetrics("c1")).thenReturn(new ContentMetricsResponse());
        mockMvc.perform(get("/api/analytics/metrics/content/c1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void topUsers_shouldReturn200() throws Exception {
        Page<UserMetricsResponse> page = new PageImpl<>(List.of(new UserMetricsResponse()), PageRequest.of(0,1), 1);
        Mockito.when(metricsService.getTopUsersByEngagement(Mockito.any())).thenReturn(page);
        mockMvc.perform(get("/api/analytics/metrics/top/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void recalcUser_shouldReturn200() throws Exception {
        mockMvc.perform(post("/api/analytics/metrics/users/u1/recalculate").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(metricsService).recalculateUserMetrics("u1");
    }

    @Test
    void platformMetrics_shouldReturn200() throws Exception {
        Mockito.when(metricsService.getPlatformMetrics()).thenReturn(Map.of("ok", true));
        mockMvc.perform(get("/api/analytics/metrics/platform").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

