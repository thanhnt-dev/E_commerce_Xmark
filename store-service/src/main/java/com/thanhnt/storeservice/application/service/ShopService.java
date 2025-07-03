package com.thanhnt.storeservice.application.service;

import com.thanhnt.storeservice.api.request.StoreCriteria;
import com.thanhnt.storeservice.application.dto.AdminSellerStatsDTO;
import com.thanhnt.storeservice.domain.entity.shop.Shop;
import org.springframework.data.domain.Page;

public interface ShopService {
  boolean existsByOwnerId(Long ownerId);

  boolean existsByEmail(String email);

  boolean existByPhone(String phone);

  Shop findByOwnerId(Long ownerId);

  Shop findById(Long id);

  Shop saveShop(Shop shop);

  Page<Shop> findByFilter(StoreCriteria criteria);

  AdminSellerStatsDTO getAdminSellerStats();
}
