package com.thanhnt.messageservice.infrastructure.service;

import com.thanhnt.messageservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.messageservice.application.dto.UserSnapshotDTO;
import com.thanhnt.messageservice.application.service.CacheService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

  @Qualifier("redisUserTemplate")
  private RedisTemplate<String, UserSnapshotDTO> userSnapshotTemplate;

  @Qualifier("redisShopTemplate")
  private RedisTemplate<String, ShopSnapshotDTO> shopSnapshotTemplate;

  public CacheServiceImpl(
      @Qualifier("redisUserTemplate") RedisTemplate<String, UserSnapshotDTO> userSnapshotTemplate,
      @Qualifier("redisShopTemplate") RedisTemplate<String, ShopSnapshotDTO> shopSnapshotTemplate) {
    this.userSnapshotTemplate = userSnapshotTemplate;
    this.shopSnapshotTemplate = shopSnapshotTemplate;
  }

  @Override
  public UserSnapshotDTO retrieveUserSnapshot(String key) {
    try {
      return this.userSnapshotTemplate.opsForValue().get(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public ShopSnapshotDTO retrieveShopSnapshot(String key) {
    try {
      return this.shopSnapshotTemplate.opsForValue().get(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Boolean hasUserKey(String key) {
    return this.userSnapshotTemplate.hasKey(key);
  }

  @Override
  public Boolean hasShopKey(String key) {
    return this.shopSnapshotTemplate.hasKey(key);
  }
}
