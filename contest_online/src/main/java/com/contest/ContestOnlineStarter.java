package com.contest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ContestOnlineStarter {
    public static void main(String[] args) {
        SpringApplication.run(ContestOnlineStarter.class);
    }
}
