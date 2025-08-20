package com.raved.analytics.it;

import com.raved.analytics.AnalyticsServiceApplication;
import com.raved.analytics.model.RankingSnapshot;
import com.raved.analytics.repository.ContentMetricsRepository;
import com.raved.analytics.repository.RankingSnapshotRepository;
import com.raved.analytics.repository.UserMetricsRepository;
import com.raved.analytics.service.RankingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AnalyticsServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(AnalyticsServiceIT.PermitAllTestSecurityConfig.class)
public class AnalyticsServiceIT {

    @LocalServerPort
    private int port;

    @Autowired private RankingService rankingService;
    @Autowired private RankingSnapshotRepository snapshotRepository;
    @Autowired private UserMetricsRepository userMetricsRepository;
    @Autowired private ContentMetricsRepository contentMetricsRepository;

    @Autowired private TestRestTemplate restTemplate;

    @Test
    void compute_and_fetch_rankings_end_to_end_smoke() {
        LocalDate d = LocalDate.now().minusDays(1);

        // Compute all flavors; repositories may be empty but methods should save empty snapshots OK
        rankingService.computeRankings("USER","ENGAGEMENT","DAILY", d, null, 10);
        rankingService.computeRankings("USER","INFLUENCE","WEEKLY", d, "science", 10);


        rankingService.computeRankings("CONTENT","VIRALITY","MONTHLY", d, "tech", 10);

        var snaps = snapshotRepository.findBySnapshotTypeAndMetricAndPeriodAndDate("USER","ENGAGEMENT","DAILY", d);
        assertThat(snaps).isNotNull();

        var resp = restTemplate.getForEntity("http://localhost:" + port + "/api/analytics/rankings/users/engagement?date=" + d + "&period=DAILY&page=0&size=10", String.class);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @TestConfiguration
    static class PermitAllTestSecurityConfig {

        @Bean
        SecurityFilterChain testChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .httpBasic(Customizer.withDefaults());
            return http.build();
        }
    }

}

