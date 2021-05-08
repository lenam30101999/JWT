package com.mcg.jwt.demo.domain.caches;


import com.mcg.jwt.demo.domain.utils.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CacheManager {

  private static final int CONFIG_EXPIRE = 10 * 60;

  @Autowired
  private  RedisTemplate<String, String> redisTemplate;

  public  void set(String key, String value) {
    redisTemplate.opsForValue().set(key, value, CONFIG_EXPIRE, TimeUnit.SECONDS);
  }

  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public <T> ArrayList<T> getList(String key, Class<T> tClass) throws Exception {
    String value = redisTemplate.opsForValue().get(key);
    return JsonParser.arrayList(value, tClass);
  }

  public <T> T get(String key, Class<T> tClass) throws Exception {
    String value = redisTemplate.opsForValue().get(key);
    return JsonParser.entity(value, tClass);
  }

  public Boolean exists(String key) {
    return redisTemplate.hasKey(key);
  }

  public void del(String key) {
    redisTemplate.delete(key);
  }

  public Set<String> keys(String pattern) {
    return redisTemplate.keys(pattern);
  }
}
