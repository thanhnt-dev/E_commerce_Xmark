package com.thanhnt.orderservice.infrastructure.service;

import com.thanhnt.orderservice.api.response.OrderDetailResponse;
import com.thanhnt.orderservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.orderservice.application.service.CacheService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

  private final RedisTemplate<String, OrderDetailResponse> redisOrderTemplate;
  private final RedisTemplate<String, ShopSnapshotDTO> redisShopTemplate;

  public CacheServiceImpl(
      @Qualifier("redisShopTemplate") RedisTemplate<String, ShopSnapshotDTO> redisShopTemplate,
      @Qualifier("redisOrderTemplate")
          RedisTemplate<String, OrderDetailResponse> redisOrderTemplate) {
    this.redisOrderTemplate = redisOrderTemplate;
    this.redisShopTemplate = redisShopTemplate;
  }

  @Override
  public void storeOrder(
      String key, OrderDetailResponse value, Integer timeOut, TimeUnit timeUnit) {
    this.redisOrderTemplate.opsForValue().set(key, value, timeOut, timeUnit);
  }

  @Override
  public OrderDetailResponse retrieveOrder(String key) {
    return this.redisOrderTemplate.opsForValue().get(key);
  }

  @Override
  public Boolean hasOrderKey(String key) {
    return this.redisOrderTemplate.hasKey(key);
  }

  @Override
  public void removeOrder(String key) {
    this.redisOrderTemplate.delete(key);
  }

  @Override
  public ShopSnapshotDTO retrieveShopSnapshot(String key) {
    try {
      return this.redisShopTemplate.opsForValue().get(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
