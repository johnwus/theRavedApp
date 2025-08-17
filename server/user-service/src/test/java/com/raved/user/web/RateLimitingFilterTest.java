package com.raved.user.web;

import com.raved.user.config.RateLimitProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RateLimitingFilterTest {

    private RateLimitingFilter filter;
    private RateLimitProperties props;

    @BeforeEach
    void setup() {
        props = new RateLimitProperties();
        RateLimitProperties.Rule rule = new RateLimitProperties.Rule();
        rule.setPath("/api/v1/auth/login");
        rule.setMethod("POST");
        rule.setKey("ip");
        rule.setCapacity(1);
        rule.setRefillPerMinute(1);
        props.setRules(List.of(rule));
        filter = new RateLimitingFilter(props);
    }

    @Test
    void exceedsCapacity_returns429() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getRequestURI()).thenReturn("/api/v1/auth/login");
        when(req.getMethod()).thenReturn("POST");
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");

        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        filter.doFilterInternal(req, resp, chain);
        filter.doFilterInternal(req, resp, chain);

        verify(resp, times(1)).setStatus(429);
        assertThat(sw.toString()).contains("RATE_LIMIT_EXCEEDED");
    }
}

