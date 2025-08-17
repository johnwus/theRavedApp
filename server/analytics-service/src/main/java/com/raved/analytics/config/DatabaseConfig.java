package com.raved.analytics.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.concurrent.TimeUnit;

/**
 * MongoDB Configuration for Analytics Service
 *
 * Configures MongoDB connection, repositories, and custom settings
 * optimized for analytics data storage and retrieval.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.raved.analytics.repository")
public class DatabaseConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database:raved_analytics}")
    private String databaseName;

    @Value("${spring.data.mongodb.connection-timeout:10000}")
    private int connectionTimeout;

    @Value("${spring.data.mongodb.socket-timeout:30000}")
    private int socketTimeout;

    @Value("${spring.data.mongodb.max-pool-size:100}")
    private int maxPoolSize;

    @Value("${spring.data.mongodb.min-pool-size:10}")
    private int minPoolSize;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder.maxSize(maxPoolSize)
                        .minSize(minPoolSize)
                        .maxWaitTime(connectionTimeout, TimeUnit.MILLISECONDS)
                        .maxConnectionIdleTime(60000, TimeUnit.MILLISECONDS)
                        .maxConnectionLifeTime(300000, TimeUnit.MILLISECONDS))
                .applyToSocketSettings(builder -> builder.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                        .readTimeout(socketTimeout, TimeUnit.MILLISECONDS))
                .applyToServerSettings(builder -> builder.heartbeatFrequency(10000, TimeUnit.MILLISECONDS)
                        .minHeartbeatFrequency(500, TimeUnit.MILLISECONDS))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate template = new MongoTemplate(mongoClient(), getDatabaseName());

        // Remove _class field from documents
        MappingMongoConverter converter = (MappingMongoConverter) template.getConverter();
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return template;
    }

    @Bean
    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(java.util.Collections.emptyList());
    }

    @Bean
    public MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
        MongoMappingContext context = new MongoMappingContext();
        context.setInitialEntitySet(getInitialEntitySet());
        context.setSimpleTypeHolder(customConversions().getSimpleTypeHolder());
        context.setFieldNamingStrategy(org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy.INSTANCE);
        context.setAutoIndexCreation(true);
        return context;
    }
}
