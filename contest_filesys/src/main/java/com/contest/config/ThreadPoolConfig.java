package com.contest.config;

import com.contest.thread.ThreadPoolFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPoolConfig {

    @Value("${thread-pool.core-pool-size}")
    private Integer corePoolSize;

    @Value("${thread-pool.max-pool-size}")
    private Integer maxPoolSize;

    @Value("${thread-pool.keep-alive-time}")
    private Long keepAliveTime;

    @Value("${thread-pool.time-unit}")
    private String timeUnit;

    @Value("${thread-pool.blocking-queue-capacity}")
    private Integer blockingQueueCapacity;

    @Bean
    public ThreadPoolFactory threadPoolFactory(){
        return new ThreadPoolFactory(
                corePoolSize,maxPoolSize,keepAliveTime,timeUnit,blockingQueueCapacity
        );
    }

}
