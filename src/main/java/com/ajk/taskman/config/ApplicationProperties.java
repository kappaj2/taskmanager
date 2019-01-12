package com.ajk.taskman.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = true)
public class ApplicationProperties {
}
