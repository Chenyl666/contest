package com.contest.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 请求中无需验证的URL前缀
 * */
@Data
@Configuration
@ConfigurationProperties(prefix = "uri.auth")
public class SkippingPathConfig {
    private List<String> skipping;
}
