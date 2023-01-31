package com.contest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ContestUserStarter {
    public static void main(String[] args) {
        SpringApplication.run(ContestUserStarter.class,args);
    }
}
