package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.domain.entity.productvariant.ProductVariant;

public interface ProductVariantService {
  ProductVariant findById(Long id);

  ProductVariant save(ProductVariant productVariant);

  ProductVariant validateProductVariant(Long productVariantId, Integer quantity, Integer version);
}
