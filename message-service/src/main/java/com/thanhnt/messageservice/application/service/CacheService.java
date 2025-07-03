package com.thanhnt.messageservice.application.service;

import com.thanhnt.messageservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.messageservice.application.dto.UserSnapshotDTO;

public interface CacheService {
  UserSnapshotDTO retrieveUserSnapshot(String key);

  ShopSnapshotDTO retrieveShopSnapshot(String key);

  Boolean hasUserKey(String key);

  Boolean hasShopKey(String key);
}
