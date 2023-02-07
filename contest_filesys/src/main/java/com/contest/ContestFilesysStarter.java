package com.contest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableDiscoveryClient
@SpringBootApplication
public class ContestFilesysStarter {
    public static void main(String[] args) {
        SpringApplication.run(ContestFilesysStarter.class,args);
    }
}
