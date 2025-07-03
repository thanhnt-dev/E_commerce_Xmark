package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.application.exception.ProductException;
import com.thanhnt.productservice.application.service.ProductAssetService;
import com.thanhnt.productservice.domain.entity.common.ErrorCode;
import com.thanhnt.productservice.domain.entity.productasset.ProductAsset;
import com.thanhnt.productservice.domain.repository.ProductAssetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductAssetServiceImpl implements ProductAssetService {
  private final ProductAssetRepository productAssetRepository;

  @Override
  public ProductAsset findProductAssetById(Long id) {
    return productAssetRepository
        .findById(id)
        .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_ASSET_NOT_FOUND));
  }

  @Override
  @Transactional
  public void saveProductAsset(ProductAsset productAsset) {
    productAssetRepository.save(productAsset);
  }
}
