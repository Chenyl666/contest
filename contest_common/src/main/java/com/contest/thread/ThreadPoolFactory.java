package com.contest.thread;

import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.*;

@AllArgsConstructor
public class ThreadPoolFactory {
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Long keepAliveTime;
    private String timeUnit;
    private Integer blockingQueueCapacity;

    private static volatile ThreadPoolExecutor threadPoolExecutor;

    public ThreadPoolExecutor getThreadPoolExecutor(){
        if(threadPoolExecutor == null){
            synchronized (ThreadPoolFactory.class){
                if(threadPoolExecutor == null){
                    threadPoolExecutor = new ThreadPoolExecutor(
                            corePoolSize,
                            maxPoolSize,
                            keepAliveTime,
                            TimeUnit.valueOf(timeUnit),
                            new LinkedBlockingQueue<>(blockingQueueCapacity),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.CallerRunsPolicy()
                    );
                }
            }
        }
        return threadPoolExecutor;
    }
}
