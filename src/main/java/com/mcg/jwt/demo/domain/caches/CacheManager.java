package com.mcg.jwt.demo.domain.caches;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class CacheManager {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String value, Integer timeout) {
        redisTemplate.opsForValue().set(
                key,
                value, Duration.ofMillis(timeout));
    }

    public String get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

}
