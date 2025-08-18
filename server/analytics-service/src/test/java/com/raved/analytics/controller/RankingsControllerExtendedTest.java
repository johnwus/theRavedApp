package com.raved.analytics.controller;

import com.raved.analytics.model.RankingSnapshot;
import com.raved.analytics.repository.RankingSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RankingsController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class RankingsControllerExtendedTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private RankingSnapshotRepository snapshotRepository;

    private RankingSnapshot makeSnap(String type, String metric, String period, LocalDate date, String category, int items) {
        RankingSnapshot snap = new RankingSnapshot();
        snap.setSnapshotType(type);
        snap.setMetric(metric);
        snap.setPeriod(period);
        snap.setDate(date);
        snap.setCategory(category);
        var list = new ArrayList<RankingSnapshot.Item>();
        for (int i = 1; i <= items; i++) {
            RankingSnapshot.Item it = new RankingSnapshot.Item();
            it.setEntityId(type.substring(0,1).toLowerCase() + i);
            it.setRank(i);
            it.setScore(BigDecimal.valueOf(100 - i));
            list.add(it);
        }
        snap.setItems(list);
        return snap;
    }

    @Test
    @WithMockUser
    void weekly_and_monthly_period_parameters_are_accepted() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        when(snapshotRepository.findBySnapshotTypeAndMetricAndPeriodAndDate("USER", "ENGAGEMENT", "WEEKLY", d))
                .thenReturn(List.of(makeSnap("USER","ENGAGEMENT","WEEKLY", d, null, 15)));
        when(snapshotRepository.findBySnapshotTypeAndMetricAndPeriodAndDate("CONTENT", "VIRALITY", "MONTHLY", d))
                .thenReturn(List.of(makeSnap("CONTENT","VIRALITY","MONTHLY", d, null, 22)));

        mockMvc.perform(get("/api/analytics/rankings/users/engagement")
                .param("date", d.toString())
                .param("period", "WEEKLY")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.period", is("WEEKLY")));

        mockMvc.perform(get("/api/analytics/rankings/content/virality")
                .param("date", d.toString())
                .param("period", "MONTHLY")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.period", is("MONTHLY")));
    }

    @Test
    @WithMockUser
    void size_greater_than_200_returns_400() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        mockMvc.perform(get("/api/analytics/rankings/users/influence")
                .param("date", d.toString())
                .param("period", "DAILY")
                .param("page", "0")
                .param("size", "201")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void category_filter_is_case_insensitive() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        when(snapshotRepository.findBySnapshotTypeAndMetricAndPeriodAndDate("CONTENT", "VIRALITY", "DAILY", d))
                .thenReturn(List.of(makeSnap("CONTENT","VIRALITY","DAILY", d, "Tech", 5)));

        mockMvc.perform(get("/api/analytics/rankings/content/virality")
                .param("date", d.toString())
                .param("period", "DAILY")
                .param("category", "tech")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems", is(5)));
    }
}

