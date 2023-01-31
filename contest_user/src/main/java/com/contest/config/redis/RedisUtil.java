package com.contest.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis读写工具类
 * */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public String get(String key){
        Object value = redisTemplate.opsForValue().get(key);
        if(value == null){
            return null;
        }
        return value.toString();
    }

    public void set(String key,String value){
        if(value == null){
            return;
        }
        redisTemplate.opsForValue().set(key,value);
    }

    public void set(String key, String value, long timeout, TimeUnit timeUnit){
        if(value == null){
            return;
        }
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

}
