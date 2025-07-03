package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.application.exception.ProductException;
import com.thanhnt.productservice.application.service.ProductVariantService;
import com.thanhnt.productservice.domain.entity.common.ErrorCode;
import com.thanhnt.productservice.domain.entity.productvariant.ProductVariant;
import com.thanhnt.productservice.domain.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
  private final ProductVariantRepository productVariantRepository;

  @Override
  public ProductVariant findById(Long id) {
    return productVariantRepository
        .findById(id)
        .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
  }

  @Override
  @Transactional
  public ProductVariant save(ProductVariant productVariant) {
    return productVariantRepository.save(productVariant);
  }

  @Override
  public ProductVariant validateProductVariant(
      Long productVariantId, Integer quantity, Integer version) {
    return productVariantRepository.findWithProduct(productVariantId, version, quantity);
  }
}
