package com.example.medcare.config;

import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {
    SqsClient sqsClient = SqsClient.builder()
                .region(Region.SA_EAST_1)
                .build();
}