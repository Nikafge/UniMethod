package com.example.diploma.integration.dspace.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DSpaceProperties.class)
public class DSpaceIntegrationConfig {
}