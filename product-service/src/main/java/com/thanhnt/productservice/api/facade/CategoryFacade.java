package com.thanhnt.productservice.api.facade;

import com.thanhnt.productservice.api.request.UpsertCategoryRequest;
import com.thanhnt.productservice.api.request.UpsertSubcategoryRequest;
import com.thanhnt.productservice.api.response.BaseResponse;
import com.thanhnt.productservice.api.response.CategoryResponse;
import com.thanhnt.productservice.api.response.SubCategoryResponse;
import java.util.List;

public interface CategoryFacade {
  BaseResponse<Void> upsertCategory(UpsertCategoryRequest request);

  BaseResponse<Void> updateCategoryStatus(Long id, boolean isActive);

  BaseResponse<List<CategoryResponse>> getAllCategories();

  BaseResponse<Void> upsertSubCategory(UpsertSubcategoryRequest request);

  BaseResponse<Void> updateSubCategoryStatus(Long id, boolean isActive);

  BaseResponse<List<SubCategoryResponse>> getAllSubCategoriesByCategoryId(Long categoryId);
}
