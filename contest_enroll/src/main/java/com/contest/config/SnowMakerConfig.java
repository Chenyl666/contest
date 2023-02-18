package com.contest.config;

import com.contest.util.SnowMaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowMakerConfig {

    @Value("${worker}")
    private Long worker;

    @Bean
    public SnowMaker snowMaker(){
        return new SnowMaker(worker);
    }

}
