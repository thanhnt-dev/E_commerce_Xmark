package com.thanhnt.orderservice.application.service;

import com.thanhnt.orderservice.api.response.OrderDetailResponse;
import com.thanhnt.orderservice.application.dto.ShopSnapshotDTO;
import java.util.concurrent.TimeUnit;

public interface CacheService {
  void storeOrder(String key, OrderDetailResponse value, Integer timeOut, TimeUnit timeUnit);

  OrderDetailResponse retrieveOrder(String key);

  Boolean hasOrderKey(String key);

  void removeOrder(String key);

  ShopSnapshotDTO retrieveShopSnapshot(String key);
}
