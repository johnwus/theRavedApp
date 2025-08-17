package com.raved.analytics.controller;

import com.raved.analytics.dto.response.RankingPageResponse;
import com.raved.analytics.model.RankingSnapshot;
import com.raved.analytics.repository.RankingSnapshotRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Pure unit tests around pagination logic (buildPage), without Spring context.
 */
public class RankingsControllerUnitTest {

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
    void buildPage_paginatesAndFilters_correctly() throws Exception {
        // Arrange
        RankingSnapshotRepository repo = mock(RankingSnapshotRepository.class);
        LocalDate d = LocalDate.now().minusDays(1);
        when(repo.findBySnapshotTypeAndMetricAndPeriodAndDate("USER", "ENGAGEMENT", "DAILY", d))
                .thenReturn(List.of(
                        makeSnap("USER", "ENGAGEMENT", "DAILY", d, null, 12),
                        makeSnap("USER", "ENGAGEMENT", "DAILY", d, null, 8)
                ));
        RankingsController controller = new RankingsController();
        // inject repo via reflection
        java.lang.reflect.Field f = RankingsController.class.getDeclaredField("snapshotRepository");
        f.setAccessible(true);
        f.set(controller, repo);

        // Act: page 1, size 10 (total 20)
        RankingPageResponse page1 = getPage(controller, "USER", "ENGAGEMENT", d, "DAILY", null, 1, 10);
        RankingPageResponse page0 = getPage(controller, "USER", "ENGAGEMENT", d, "DAILY", null, 0, 10);

        // Assert
        assertThat(page0.getTotalItems()).isEqualTo(20);
        assertThat(page0.getItems()).hasSize(10);
        assertThat(page1.getItems()).hasSize(10);

        // With category filter
        when(repo.findBySnapshotTypeAndMetricAndPeriodAndDate("USER", "INFLUENCE", "DAILY", d))
                .thenReturn(List.of(makeSnap("USER", "INFLUENCE", "DAILY", d, "science", 15)));
        RankingPageResponse cat = getPage(controller, "USER", "INFLUENCE", d, "DAILY", "science", 0, 10);
        assertThat(cat.getTotalItems()).isEqualTo(15);
        assertThat(cat.getItems()).hasSize(10);
    }

    private RankingPageResponse getPage(RankingsController controller, String type, String metric, LocalDate date, String period, String category, int page, int size) throws Exception {
        // call private buildPage via reflection to test it directly
        var m = RankingsController.class.getDeclaredMethod("buildPage", String.class, String.class, LocalDate.class, String.class, String.class, int.class, int.class);
        m.setAccessible(true);
        return (RankingPageResponse) m.invoke(controller, type, metric, date, period, category, page, size);
    }
}

