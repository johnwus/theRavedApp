package com.raved.analytics.controller;

import com.raved.analytics.dto.response.AnalyticsSearchHitResponse;
import com.raved.analytics.service.AnalyticsService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnalyticsController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class AnalyticsControllerSearchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @Test
    void searchByUser_shouldReturn200() throws Exception {
        AnalyticsSearchHitResponse hit = new AnalyticsSearchHitResponse();
        hit.setId("e1");
        hit.setUserId("u1");
        Page<AnalyticsSearchHitResponse> page = new PageImpl<>(List.of(hit), PageRequest.of(0, 1), 1);
        Mockito.when(analyticsService.searchByUser(Mockito.eq("u1"), Mockito.any())).thenReturn(page);

        mockMvc.perform(get("/api/analytics/search/user/u1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void searchByHashtag_shouldReturn200() throws Exception {
        AnalyticsSearchHitResponse hit = new AnalyticsSearchHitResponse();
        hit.setId("e2");
        hit.setHashtags("raved");
        Page<AnalyticsSearchHitResponse> page = new PageImpl<>(List.of(hit), PageRequest.of(0, 1), 1);
        Mockito.when(analyticsService.searchByHashtag(Mockito.eq("raved"), Mockito.any())).thenReturn(page);

        mockMvc.perform(get("/api/analytics/search/hashtags?q=raved").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}

