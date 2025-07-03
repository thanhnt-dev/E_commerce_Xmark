package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.domain.entity.productasset.ProductAsset;

public interface ProductAssetService {
  ProductAsset findProductAssetById(Long id);

  void saveProductAsset(ProductAsset productAsset);
}
