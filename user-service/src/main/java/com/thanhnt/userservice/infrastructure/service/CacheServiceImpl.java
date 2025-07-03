package com.thanhnt.userservice.infrastructure.service;

import com.thanhnt.userservice.application.dto.UserSnapshotDTO;
import com.thanhnt.userservice.application.service.CacheService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

  private final RedisTemplate<String, Object> redisTemplate;
  private final RedisTemplate<String, UserSnapshotDTO> redisUserTemplate;

  @Override
  public void store(String key, Object value, Integer timeOut, TimeUnit timeUnit) {
    this.redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
  }

  @Override
  public Object retrieve(String key) {
    return this.redisTemplate.opsForValue().get(key);
  }

  @Override
  public Boolean hasKey(String key) {
    return this.redisTemplate.hasKey(key);
  }

  @Override
  public void remove(String key) {
    this.redisTemplate.delete(key);
  }

  @Override
  public void storeUser(String key, UserSnapshotDTO value) {
    this.redisUserTemplate.opsForValue().set(key, (UserSnapshotDTO) value);
  }

  @Override
  public Boolean hasUserKey(String key) {
    return this.redisUserTemplate.hasKey(key);
  }

  @Override
  public void removeUser(String key) {
    this.redisUserTemplate.delete(key);
  }
}
