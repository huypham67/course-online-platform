package com.fullstack.online_course_platform.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final AwsS3Properties s3Properties;

    @Value("${aws.credentials.access-key:}")
    private String accessKey;

    @Value("${aws.credentials.secret-key:}")
    private String secretKey;

    private AwsCredentialsProvider getCredentialsProvider() {
        if (accessKey != null && !accessKey.isBlank() && secretKey != null && !secretKey.isBlank()) {
            return StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
            );
        }
        try {
            return DefaultCredentialsProvider.create();
        } catch (Exception e) {
            return StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("dummy-access-key", "dummy-secret-key")
            );
        }
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(s3Properties.getRegion()))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(s3Properties.getRegion()))
                .credentialsProvider(getCredentialsProvider())
                .build();
    }
}
