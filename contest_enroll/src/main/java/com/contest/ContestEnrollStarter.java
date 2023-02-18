package com.contest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ContestEnrollStarter {
    public static void main(String[] args) {
        SpringApplication.run(ContestEnrollStarter.class,args);
    }
}
