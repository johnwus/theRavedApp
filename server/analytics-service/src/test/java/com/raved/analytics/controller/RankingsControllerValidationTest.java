package com.raved.analytics.controller;

import com.raved.analytics.repository.RankingSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RankingsController.class)
public class RankingsControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RankingSnapshotRepository snapshotRepository;

    @Test
    @WithMockUser
    void invalid_date_format_returns_400() throws Exception {
        mockMvc.perform(get("/api/analytics/rankings/users/engagement")
                .param("date", "2025-13-99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void invalid_period_returns_400() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        mockMvc.perform(get("/api/analytics/rankings/content/virality")
                .param("date", d.toString())
                .param("period", "HOURLY")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unauthenticated_access_returns_401() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        mockMvc.perform(get("/api/analytics/rankings/users/engagement")
                .param("date", d.toString())
                .param("period", "DAILY")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void invalid_pagination_returns_400() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        mockMvc.perform(get("/api/analytics/rankings/users/influence")
                .param("date", d.toString())
                .param("period", "DAILY")
                .param("page", "-1")
                .param("size", "1000")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void null_or_empty_category_is_ignored_not_error() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        mockMvc.perform(get("/api/analytics/rankings/users/engagement")
                .param("date", d.toString())
                .param("period", "DAILY")
                .param("category", "")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
