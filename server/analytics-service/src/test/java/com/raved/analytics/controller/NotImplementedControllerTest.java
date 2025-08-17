package com.raved.analytics.controller;

import com.raved.analytics.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnalyticsController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class NotImplementedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @Test
    void getUserBehaviorPatterns_returns501() throws Exception {
        when(analyticsService.getUserBehaviorPatterns("u1")).thenThrow(new com.raved.analytics.exception.NotImplementedFeatureException("User behavior patterns not implemented yet"));
        mockMvc.perform(get("/api/analytics/users/u1/behavior").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotImplemented());
    }
}

