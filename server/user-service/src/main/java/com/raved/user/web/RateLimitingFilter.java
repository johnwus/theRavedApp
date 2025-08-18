package com.raved.user.web;

import com.raved.user.config.RateLimitProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    private final RateLimitProperties props;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Simple in-memory counters keyed by (ruleKey, minuteBucket)
    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    @Autowired
    public RateLimitingFilter(RateLimitProperties props) {
        this.props = props;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!props.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        String method = request.getMethod();

        RateLimitProperties.Rule matched = props.getRules().stream()
                .filter(r -> method.equalsIgnoreCase(r.getMethod()))
                .filter(r -> pathMatcher.match(r.getPath(), path))
                .findFirst().orElse(null);

        if (matched == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String keyComponent;
        if (Objects.equals(matched.getKey(), "destination")) {
            // For verification code endpoints, destination is email/phone in body
            keyComponent = request.getRemoteAddr();
        } else {
            keyComponent = request.getRemoteAddr();
        }

        long minuteBucket = Instant.now().getEpochSecond() / 60;
        String counterKey = matched.getPath() + "|" + matched.getMethod() + "|" + matched.getKey() + "|" + keyComponent + "|" + minuteBucket;
        counters.putIfAbsent(counterKey, new AtomicInteger(0));
        int current = counters.get(counterKey).incrementAndGet();

        if (current > matched.getCapacity()) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false,\"error\":{\"code\":\"RATE_LIMIT_EXCEEDED\",\"message\":\"Too many requests\"}}\n");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

