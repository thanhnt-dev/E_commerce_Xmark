package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.domain.entity.brand.Brand;
import java.util.List;

public interface BrandService {
  Brand findById(Long id);

  List<Brand> findAll();
}
