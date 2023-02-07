package com.contest.config.thread;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class ThreadPoolConfig {

    @Value("${thread-pool.core-pool-size}")
    private Integer corePoolSize;

    @Value("${thread-pool.max-pool-size}")
    private Integer maxPoolSize;

    @Value("${thread-pool.blocking-queue-capacity}")
    private Integer blockingQueueCapacity;

    @Value("${keep-alive-time}")
    private Long keepAliveTime;

    private volatile ThreadPoolExecutor poolExecutor;

    private ThreadPoolExecutor getThreadPoolExecutor(){
        if(poolExecutor == null){
            synchronized (ThreadPoolConfig.class){
                if(poolExecutor == null){
                    poolExecutor = new ThreadPoolExecutor(
                            corePoolSize,
                            maxPoolSize,
                            keepAliveTime,
                            TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<>(),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.CallerRunsPolicy()
                    );
                }
            }
        }
        return poolExecutor;
    }

    public void submit(Runnable task){
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        threadPoolExecutor.submit(task);
    }

    public <T> T submit(Callable<T> task) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        Future<T> future = threadPoolExecutor.submit(task);
        return future.get();
    }

}
