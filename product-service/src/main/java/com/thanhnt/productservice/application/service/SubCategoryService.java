package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.domain.entity.subcategory.SubCategory;

public interface SubCategoryService {
  SubCategory findById(Long id);
}
