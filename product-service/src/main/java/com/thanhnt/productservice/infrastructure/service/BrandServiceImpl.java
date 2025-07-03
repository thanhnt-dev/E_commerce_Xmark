package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.application.exception.StoreException;
import com.thanhnt.productservice.application.service.BrandService;
import com.thanhnt.productservice.domain.entity.brand.Brand;
import com.thanhnt.productservice.domain.entity.common.ErrorCode;
import com.thanhnt.productservice.domain.repository.BrandRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BrandServiceImpl implements BrandService {

  private final BrandRepository brandRepository;

  @Override
  public Brand findById(Long id) {
    return brandRepository
        .findById(id)
        .orElseThrow(() -> new StoreException(ErrorCode.BRAND_NOT_FOUND));
  }

  @Override
  public List<Brand> findAll() {
    return brandRepository.findAll();
  }
}
