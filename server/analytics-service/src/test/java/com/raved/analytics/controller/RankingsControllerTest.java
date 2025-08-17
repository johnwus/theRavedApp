package com.raved.analytics.controller;

import com.raved.analytics.model.RankingSnapshot;
import com.raved.analytics.repository.RankingSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RankingsController.class)
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
public class RankingsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RankingSnapshotRepository snapshotRepository;

    private RankingSnapshot makeSnap(String type, String metric, String period, LocalDate date, String category, int items) {
        RankingSnapshot snap = new RankingSnapshot();
        snap.setSnapshotType(type);
        snap.setMetric(metric);
        snap.setPeriod(period);
        snap.setDate(date);
        snap.setCategory(category);
        var list = new java.util.ArrayList<RankingSnapshot.Item>();
        for (int i = 1; i <= items; i++) {
            RankingSnapshot.Item it = new RankingSnapshot.Item();
            it.setEntityId(type.substring(0, 1).toLowerCase() + i);
            it.setRank(i);
            it.setScore(BigDecimal.valueOf(100 - i));
            list.add(it);
        }
        snap.setItems(list);
        return snap;
    }

    @Test
    void usersEngagement_paginationAndStructure_ok() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        when(snapshotRepository.findBySnapshotTypeAndMetricAndPeriodAndDate("USER", "ENGAGEMENT", "DAILY", d))
                .thenReturn(java.util.List.of(makeSnap("USER", "ENGAGEMENT", "DAILY", d, null, 120)));

        mockMvc.perform(get("/api/analytics/rankings/users/engagement")
                .param("date", d.toString())
                .param("period", "DAILY")
                .param("page", "1")
                .param("size", "25")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.snapshotType", is("USER")))
                .andExpect(jsonPath("$.metric", is("ENGAGEMENT")))
                .andExpect(jsonPath("$.period", is("DAILY")))
                .andExpect(jsonPath("$.page", is(1)))
                .andExpect(jsonPath("$.size", is(25)))
                .andExpect(jsonPath("$.totalItems", is(120)))
                .andExpect(jsonPath("$.items", hasSize(25)));
    }

    @Test
    void usersInfluence_categoryFilter_ok() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        when(snapshotRepository.findBySnapshotTypeAndMetricAndPeriodAndDate("USER", "INFLUENCE", "DAILY", d))
                .thenReturn(java.util.List.of(makeSnap("USER", "INFLUENCE", "DAILY", d, "science", 50)));

        mockMvc.perform(get("/api/analytics/rankings/users/influence")
                .param("date", d.toString())
                .param("period", "DAILY")
                .param("category", "science")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.snapshotType", is("USER")))
                .andExpect(jsonPath("$.metric", is("INFLUENCE")))
                .andExpect(jsonPath("$.items", hasSize(10)))
                .andExpect(jsonPath("$.totalItems", is(50)));
    }

    @Test
    void contentVirality_periodAndDate_ok() throws Exception {
        LocalDate d = LocalDate.now().minusDays(1);
        when(snapshotRepository.findBySnapshotTypeAndMetricAndPeriodAndDate("CONTENT", "VIRALITY", "DAILY", d))
                .thenReturn(java.util.List.of(makeSnap("CONTENT", "VIRALITY", "DAILY", d, "tech", 80)));

        mockMvc.perform(get("/api/analytics/rankings/content/virality")
                .param("date", d.toString())
                .param("period", "DAILY")
                .param("page", "0")
                .param("size", "30")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.snapshotType", is("CONTENT")))
                .andExpect(jsonPath("$.metric", is("VIRALITY")))
                .andExpect(jsonPath("$.items", hasSize(30)))
                .andExpect(jsonPath("$.totalItems", is(80)));
    }
}
