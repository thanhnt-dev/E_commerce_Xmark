package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.application.exception.StoreException;
import com.thanhnt.productservice.application.service.SubCategoryService;
import com.thanhnt.productservice.domain.entity.common.ErrorCode;
import com.thanhnt.productservice.domain.entity.subcategory.SubCategory;
import com.thanhnt.productservice.domain.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

  private final SubCategoryRepository subCategoryRepository;

  @Override
  public SubCategory findById(Long id) {
    return subCategoryRepository
        .findById(id)
        .orElseThrow(() -> new StoreException(ErrorCode.SUB_CATEGORY_NOT_FOUND));
  }
}
