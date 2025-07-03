package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.application.exception.CategoryException;
import com.thanhnt.productservice.application.service.CategoryService;
import com.thanhnt.productservice.domain.entity.category.Category;
import com.thanhnt.productservice.domain.entity.common.ErrorCode;
import com.thanhnt.productservice.domain.entity.subcategory.SubCategory;
import com.thanhnt.productservice.domain.repository.CategoryRepository;
import com.thanhnt.productservice.domain.repository.SubCategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final SubCategoryRepository subCategoryRepository;

  @Override
  @Transactional
  public void upsertCategory(Category category) {
    categoryRepository.save(category);
  }

  @Override
  public Category getCategoryById(Long id) {
    return categoryRepository
        .findById(id)
        .orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));
  }

  @Override
  public SubCategory getSubCategoryById(Long id) {
    return subCategoryRepository
        .findById(id)
        .orElseThrow(() -> new CategoryException(ErrorCode.SUBCATEGORY_NOT_FOUND));
  }

  @Override
  public List<Category> getAllCategoriesByParentId(Long parentId) {
    return categoryRepository.findByParentId(parentId);
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public void upsertSubCategory(SubCategory subCategory) {
    subCategoryRepository.save(subCategory);
  }

  @Override
  public List<SubCategory> getAllSubCategoriesByCategoryId(Long categoryId) {
    return subCategoryRepository.findSubCategoryByCategoryId(categoryId);
  }
}
