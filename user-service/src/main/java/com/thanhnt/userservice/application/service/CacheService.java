package com.thanhnt.userservice.application.service;

import com.thanhnt.userservice.application.dto.UserSnapshotDTO;
import java.util.concurrent.TimeUnit;

public interface CacheService {
  void store(String key, Object value, Integer timeOut, TimeUnit timeUnit);

  Object retrieve(String key);

  Boolean hasKey(String key);

  void remove(String key);

  void storeUser(String key, UserSnapshotDTO value);

  Boolean hasUserKey(String key);

  void removeUser(String key);
}
