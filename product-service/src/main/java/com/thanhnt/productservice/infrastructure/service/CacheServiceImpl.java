package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.productservice.application.service.CacheService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {
  private final RedisTemplate<String, ShopSnapshotDTO> redisShopTemplate;
  private final RedisTemplate<String, Object> redisProductTemplate;

  public CacheServiceImpl(
      @Qualifier("redisShopTemplate") RedisTemplate<String, ShopSnapshotDTO> redisShopTemplate,
      @Qualifier("redisProductTemplate") RedisTemplate<String, Object> redisProductTemplate) {
    this.redisShopTemplate = redisShopTemplate;
    this.redisProductTemplate = redisProductTemplate;
  }

  @Override
  public void storeShop(String key, ShopSnapshotDTO value) {
    this.redisShopTemplate.opsForValue().set(key, value);
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

  @Override
  public Boolean hasShopKey(String key) {
    return this.redisShopTemplate.hasKey(key);
  }

  @Override
  public void removeShop(String key) {
    this.redisShopTemplate.delete(key);
  }

  @Override
  public Object retrieveProduct(String key) {
    return redisProductTemplate.opsForValue().get(key);
  }

  @Override
  public void storeProductAndIncreaseView(String key) {
    this.redisProductTemplate.opsForValue().increment(key, 1);
  }

  @Override
  public Boolean hasProductKey(String key) {
    return this.redisProductTemplate.hasKey(key);
  }

  @Override
  public void removeProduct(String key) {
    this.redisProductTemplate.delete(key);
  }

  @Override
  public Set<String> getAllProductViewKeys(String key) {
    return redisProductTemplate.keys(key);
  }
}
