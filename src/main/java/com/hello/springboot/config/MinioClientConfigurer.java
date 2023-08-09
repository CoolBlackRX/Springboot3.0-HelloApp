package com.hello.springboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "minio")
@Configuration
@EnableConfigurationProperties(MinioClientConfigurer.class)
public class MinioClientConfigurer {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String namespace;
}
