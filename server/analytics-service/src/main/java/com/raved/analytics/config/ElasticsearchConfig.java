package com.raved.analytics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch Configuration for Analytics Service Enables Elasticsearch
 * client and repositories only when profile "it-es" is active.
 */
@Configuration
@Profile("it-es")
@EnableElasticsearchRepositories(basePackages = "com.raved.analytics.repository.elasticsearch")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.data.elasticsearch.uris:http://localhost:9200}")
    private String elasticsearchUris;

    @Value("${spring.data.elasticsearch.username:}")
    private String username;

    @Value("${spring.data.elasticsearch.password:}")
    private String password;

    @Value("${spring.data.elasticsearch.connection-timeout:5s}")
    private String connectionTimeout;

    @Value("${spring.data.elasticsearch.socket-timeout:30s}")
    private String socketTimeout;

    @Override
    public ClientConfiguration clientConfiguration() {
        var builder = ClientConfiguration.builder()
                .connectedTo(elasticsearchUris.replace("http://", "").replace("https://", ""))
                .withConnectTimeout(java.time.Duration.parse("PT" + connectionTimeout.toUpperCase()))
                .withSocketTimeout(java.time.Duration.parse("PT" + socketTimeout.toUpperCase()));

        // Add authentication if credentials are provided
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            builder.withBasicAuth(username, password);
        }

        return builder.build();
    }
}
