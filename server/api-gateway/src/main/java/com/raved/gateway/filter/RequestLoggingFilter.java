package com.raved.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class RequestLoggingFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 10)
    public GlobalFilter loggingGlobalFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.debug("{} {}", request.getMethod(), request.getURI());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                int status = exchange.getResponse().getStatusCode() != null
                    ? exchange.getResponse().getStatusCode().value()
                    : 200;
                log.debug("<- {} {} => {}", request.getMethod(), request.getURI(), status);
            }));
        };
    }
}


