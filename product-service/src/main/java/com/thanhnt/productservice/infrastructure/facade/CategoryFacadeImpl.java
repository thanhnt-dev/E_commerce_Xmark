package com.thanhnt.productservice.infrastructure.facade;

import com.thanhnt.productservice.api.facade.CategoryFacade;
import com.thanhnt.productservice.api.request.UpsertCategoryRequest;
import com.thanhnt.productservice.api.request.UpsertSubcategoryRequest;
import com.thanhnt.productservice.api.response.BaseResponse;
import com.thanhnt.productservice.api.response.CategoryChildResponse;
import com.thanhnt.productservice.api.response.CategoryResponse;
import com.thanhnt.productservice.api.response.SubCategoryResponse;
import com.thanhnt.productservice.application.exception.CategoryException;
import com.thanhnt.productservice.application.service.CategoryService;
import com.thanhnt.productservice.domain.entity.category.Category;
import com.thanhnt.productservice.domain.entity.common.ErrorCode;
import com.thanhnt.productservice.domain.entity.subcategory.SubCategory;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryFacadeImpl implements CategoryFacade {

  private final CategoryService categoryService;

  @Override
  @Transactional
  public BaseResponse<Void> upsertCategory(UpsertCategoryRequest request) {
    log.info("Upserting category with request: {}", request.toString());
    Category childCategory = null;
    if (request.getParentId() != null) {
      childCategory = categoryService.getCategoryById(request.getParentId());
    }
    if (request.getRequestType().isCreate()) {
      log.info("Creating new category: {}", request.getName());
      categoryService.upsertCategory(
          Category.builder()
              .categoryName(request.getName())
              .description(request.getDescription())
              .parent(childCategory)
              .build());
      return BaseResponse.ok();
    }
    Category category = categoryService.getCategoryById(request.getId());
    category.updateCategory(request.getName(), request.getDescription(), childCategory);
    categoryService.upsertCategory(category);
    log.info("Updated category: {}", request.getId());
    return BaseResponse.ok();
  }

  @Override
  @Transactional
  public BaseResponse<Void> updateCategoryStatus(Long id, boolean isActive) {
    log.info("Updating category status for id: {}, isActive: {}", id, isActive);
    Category category = categoryService.getCategoryById(id);
    category.updateStatus(isActive);
    categoryService.upsertCategory(category);
    log.info("Category status updated successfully for id: {}", id);
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<List<CategoryResponse>> getAllCategories() {
    log.info("Fetching all categories from the database");
    List<Category> level1Categories = categoryService.getAllCategories();

    if (level1Categories.isEmpty()) {
      throw new CategoryException(ErrorCode.CATEGORY_NOT_FOUND);
    }

    List<Category> level1CategoriesFilter =
        level1Categories.stream().filter(c -> c.getParent() == null).toList();

    List<CategoryResponse> responses =
        level1CategoriesFilter.stream()
            .map(
                level1 ->
                    CategoryResponse.builder()
                        .id(level1.getId())
                        .name(level1.getCategoryName())
                        .description(level1.getDescription())
                        .isActive(level1.isActive())
                        .children(
                            level1.getChildren().stream()
                                .map(
                                    level2 ->
                                        CategoryChildResponse.builder()
                                            .id(level2.getId())
                                            .name(level2.getCategoryName())
                                            .description(level2.getDescription())
                                            .isActive(level2.isActive())
                                            .build())
                                .toList())
                        .build())
            .toList();
    log.info("Successfully fetched {} categories", responses.size());

    return BaseResponse.build(responses, true);
  }

  @Override
  @Transactional
  public BaseResponse<Void> upsertSubCategory(UpsertSubcategoryRequest request) {
    log.info("Upserting subcategory with request: {}", request.toString());
    Category category = categoryService.getCategoryById(request.getCategoryId());
    if (request.getRequestType().isCreate()) {
      categoryService.upsertSubCategory(
          SubCategory.builder()
              .subCategoryName(request.getName())
              .description(request.getDescription())
              .category(category)
              .build());
      log.info("Created new subcategory: {}", request.getName());
      return BaseResponse.ok();
    }
    SubCategory subCategory = categoryService.getSubCategoryById(request.getId());
    subCategory.updateSubCategory(request.getName(), request.getDescription(), category);
    categoryService.upsertSubCategory(subCategory);
    log.info("Updated subcategory: {}", request.getId());
    return BaseResponse.ok();
  }

  @Override
  @Transactional
  public BaseResponse<Void> updateSubCategoryStatus(Long id, boolean isActive) {
    log.info("Updating subcategory status for id: {}, isActive: {}", id, isActive);
    SubCategory subCategory = categoryService.getSubCategoryById(id);
    subCategory.updateStatus(isActive);
    categoryService.upsertSubCategory(subCategory);
    return BaseResponse.ok();
  }

  @Override
  public BaseResponse<List<SubCategoryResponse>> getAllSubCategoriesByCategoryId(Long categoryId) {
    log.info("Fetching all subcategories for category id: {}", categoryId);
    List<SubCategory> subCategories = categoryService.getAllSubCategoriesByCategoryId(categoryId);
    if (subCategories.isEmpty()) {
      throw new CategoryException(ErrorCode.SUBCATEGORY_NOT_FOUND);
    }
    List<SubCategoryResponse> subCategoryResponses =
        subCategories.stream()
            .map(
                subCategory ->
                    SubCategoryResponse.builder()
                        .id(subCategory.getId())
                        .name(subCategory.getSubCategoryName())
                        .description(subCategory.getDescription())
                        .isActive(subCategory.isActive())
                        .build())
            .toList();
    log.info(
        "Successfully fetched {} subcategories for category id: {}",
        subCategoryResponses.size(),
        categoryId);
    return BaseResponse.build(subCategoryResponses, true);
  }
}
