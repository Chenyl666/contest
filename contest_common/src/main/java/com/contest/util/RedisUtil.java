package com.contest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Redis读写工具类
 * */
public class RedisUtil {

    private RedisTemplate<String,Object> redisTemplate;

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String get(String key){
        Object value = redisTemplate.opsForValue().get(key);
        if(value == null){
            return null;
        }
        return value.toString();
    }

    public Object getForObject(String key){
        Optional<Object> valueOptional = Optional.ofNullable(redisTemplate.opsForValue().get(key));
        return valueOptional.orElse(null);
    }

    public void set(String key,Object value){
        if(value == null){
            return;
        }
        redisTemplate.opsForValue().set(key,value);
    }

    public void set(String key, Object value, long timeout, TimeUnit timeUnit){
        if(value == null){
            return;
        }
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

}
