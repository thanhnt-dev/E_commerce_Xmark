package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.application.dto.ShopSnapshotDTO;
import java.util.Set;

public interface CacheService {
  void storeShop(String key, ShopSnapshotDTO value);

  ShopSnapshotDTO retrieveShopSnapshot(String key);

  Boolean hasShopKey(String key);

  void removeShop(String key);

  Object retrieveProduct(String key);

  void storeProductAndIncreaseView(String key);

  Boolean hasProductKey(String key);

  void removeProduct(String key);

  Set<String> getAllProductViewKeys(String key);
}
