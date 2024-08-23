package com.test.openai.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Bean
    public S3Client amazonS3Client(
            final AwsCredentialsProvider credentialsProvider,
            final AwsRegionProvider regionProvider
    ) {
        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(regionProvider.getRegion())
                .build();
    }

}