package com.thanhnt.storeservice.infrastructure.service;

import com.thanhnt.storeservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.storeservice.application.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {
  private final RedisTemplate<String, ShopSnapshotDTO> redisShopTemplate;

  @Override
  public void storeShop(String key, ShopSnapshotDTO value) {
    this.redisShopTemplate.opsForValue().set(key, value);
  }

  @Override
  public Boolean hasShopKey(String key) {
    return this.redisShopTemplate.hasKey(key);
  }

  @Override
  public void removeShop(String key) {
    this.redisShopTemplate.delete(key);
  }
}
