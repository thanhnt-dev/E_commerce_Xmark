package com.thanhnt.storeservice.infrastructure.service;

import com.thanhnt.storeservice.application.service.ShopProductService;
import com.thanhnt.storeservice.domain.entity.shopproduct.ShopProducts;
import com.thanhnt.storeservice.domain.repository.ShopProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopProductServiceImpl implements ShopProductService {
  private final ShopProductsRepository shopProductsRepository;

  @Override
  public void saveShopProduct(ShopProducts shopProducts) {
    shopProductsRepository.save(shopProducts);
  }
}
