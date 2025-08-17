package com.raved.analytics.it;

import com.raved.analytics.model.RankingSnapshot;
import com.raved.analytics.repository.RankingSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/raved_analytics_test"
})
public class RankingsEndpointIT {

    @LocalServerPort int port;
    @Autowired RankingSnapshotRepository repo;

    @Test
    void get_rankings_end_to_end_with_real_repo() {
        repo.deleteAll();
        LocalDate d = LocalDate.now().minusDays(1);
        repo.saveAll(List.of(
                snap("USER","ENGAGEMENT","DAILY", d, null, 40),
                snap("CONTENT","VIRALITY","DAILY", d, "tech", 25)
        ));
        RestTemplate rt = new RestTemplate();
        String base = "http://localhost:" + port + "/api/analytics/rankings";
        ResponseEntity<String> resp = rt.getForEntity(base + "/users/engagement?date="+d+"&period=DAILY&page=0&size=20", String.class);
        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        ResponseEntity<String> resp2 = rt.getForEntity(base + "/content/virality?date="+d+"&period=DAILY&page=1&size=10", String.class);
        assertThat(resp2.getStatusCode().value()).isEqualTo(200);
    }

    private RankingSnapshot snap(String type, String metric, String period, LocalDate date, String category, int items) {
        RankingSnapshot s = new RankingSnapshot();
        s.setSnapshotType(type);
        s.setMetric(metric);
        s.setPeriod(period);
        s.setDate(date);
        s.setCategory(category);
        var list = new java.util.ArrayList<RankingSnapshot.Item>();
        for (int i=1;i<=items;i++) {
            RankingSnapshot.Item it = new RankingSnapshot.Item();
            it.setEntityId(type.substring(0,1).toLowerCase()+i);
            it.setRank(i);
            it.setScore(BigDecimal.valueOf(100-i));
            list.add(it);
        }
        s.setItems(list);
        return s;
    }
}

