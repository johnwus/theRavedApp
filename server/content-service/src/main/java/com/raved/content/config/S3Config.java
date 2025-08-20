package com.raved.content.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * S3Config for TheRavedApp MongoDB with S3 media storage
 * Updated to use AWS SDK v2
 */
@Configuration
public class S3Config {

    @Value("${aws.access.key.id:your-access-key}")
    private String accessKeyId;

    @Value("${aws.secret.access.key:your-secret-key}")
    private String secretAccessKey;

    @Value("${aws.s3.region:us-east-1}")
    private String region;

    @Value("${aws.s3.bucket.name:raved-content-media}")
    private String bucketName;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public String s3BucketName() {
        return bucketName;
    }
}
