package com.thanhnt.storeservice.application.service;

import com.thanhnt.storeservice.application.dto.ShopSnapshotDTO;

public interface CacheService {
  void storeShop(String key, ShopSnapshotDTO value);

  Boolean hasShopKey(String key);

  void removeShop(String key);
}
