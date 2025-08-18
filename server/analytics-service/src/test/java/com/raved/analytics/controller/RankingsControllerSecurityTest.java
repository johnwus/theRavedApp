package com.raved.analytics.controller;

import com.raved.analytics.config.TestSecurityConfig;
import com.raved.analytics.repository.RankingSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RankingsController.class)
@Import(TestSecurityConfig.class)
public class RankingsControllerSecurityTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private RankingSnapshotRepository snapshotRepository;

    @Test
    void unauthenticated_requests_are_401() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        mockMvc.perform(get("/api/analytics/rankings/users/engagement")
                .param("date", d.toString())
                .param("period", "DAILY")
                .param("page", "0").param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void authenticated_requests_are_200() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        mockMvc.perform(get("/api/analytics/rankings/users/engagement")
                .param("date", d.toString())
                .param("period", "DAILY")
                .param("page", "0").param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

