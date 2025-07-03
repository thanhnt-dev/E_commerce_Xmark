package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.domain.entity.category.Category;
import com.thanhnt.productservice.domain.entity.subcategory.SubCategory;
import java.util.List;

public interface CategoryService {
  void upsertCategory(Category category);

  Category getCategoryById(Long id);

  SubCategory getSubCategoryById(Long id);

  List<Category> getAllCategoriesByParentId(Long parentId);

  List<Category> getAllCategories();

  void upsertSubCategory(SubCategory category);

  List<SubCategory> getAllSubCategoriesByCategoryId(Long categoryId);
}
