package com.fullstack.online_course_platform.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3")
@Getter
@Setter
public class AwsS3Properties {
    private String bucketName = "online-course-platform-bucket";
    private String region = "ap-southeast-1";
    private int presignedExpiryMinutes = 5;
    private int presignedGetExpiryMinutes = 60;
    private String publicBaseUrl = "https://online-course-platform-bucket.s3.ap-southeast-1.amazonaws.com";
}
