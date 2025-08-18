package com.raved.content.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import com.amazonaws.auth.AWSStaticCredentialsProvider;
// import com.amazonaws.auth.BasicAWSCredentials;
// import com.amazonaws.services.s3.AmazonS3;
// import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * S3Config for TheRavedApp MongoDB with S3 media storage
 * Temporarily commented out due to missing AWS dependencies
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

    // TODO: Uncomment when AWS SDK dependencies are added
    /*
     * @Bean
     * public AmazonS3 amazonS3Client() {
     * BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId,
     * secretAccessKey);
     * 
     * return AmazonS3ClientBuilder.standard()
     * .withRegion(region)
     * .withCredentials(new AWSStaticCredentialsProvider(credentials))
     * .build();
     * }
     */

    @Bean
    public String s3BucketName() {
        return bucketName;
    }
}
