package com.contest.async.pool;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 动态定时任务线程池
 * */
@Component
public class TaskExecutor {

    @Resource
    private  ScheduledExecutorService scheduledExecutorService;

    public void addTask(Runnable task,Date runDate){
        long runTimeStamp = runDate.getTime();
        long currentTimeStamp = System.currentTimeMillis();
        if(runTimeStamp < currentTimeStamp){
            throw new RuntimeException("run time is less than current time");
        }
        scheduledExecutorService.schedule(task,runTimeStamp-currentTimeStamp,TimeUnit.MILLISECONDS);
    }

}
