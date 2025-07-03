package com.thanhnt.productservice.infrastructure.rest.controller;

import com.thanhnt.productservice.api.facade.CategoryFacade;
import com.thanhnt.productservice.api.request.UpsertCategoryRequest;
import com.thanhnt.productservice.api.request.UpsertSubcategoryRequest;
import com.thanhnt.productservice.api.response.BaseResponse;
import com.thanhnt.productservice.api.response.CategoryResponse;
import com.thanhnt.productservice.api.response.SubCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryFacade categoryFacade;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Upsert category",
      tags = {"Categories APIs"})
  public BaseResponse<Void> upsertCategory(@RequestBody @Validated UpsertCategoryRequest request) {
    return this.categoryFacade.upsertCategory(request);
  }

  @PutMapping("/{id}/status")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Update status category",
      tags = {"Categories APIs"})
  public BaseResponse<Void> updateCategoryStatus(
      @PathVariable Long id, @RequestParam boolean isActive) {
    return this.categoryFacade.updateCategoryStatus(id, isActive);
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get all categories",
      tags = {"Categories APIs"})
  public BaseResponse<List<CategoryResponse>> getAllCategories() {
    return this.categoryFacade.getAllCategories();
  }

  @PostMapping("/sub-categories")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Upsert subcategory",
      tags = {"Categories APIs"})
  public BaseResponse<Void> upsertSubCategory(@RequestBody UpsertSubcategoryRequest request) {
    return this.categoryFacade.upsertSubCategory(request);
  }

  @PutMapping("/sub-categories/{id}/status")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Update status subcategory",
      tags = {"Categories APIs"})
  public BaseResponse<Void> updateSubCategoryStatus(
      @PathVariable Long id, @RequestParam boolean isActive) {

    return this.categoryFacade.updateSubCategoryStatus(id, isActive);
  }

  @GetMapping("/{categoryId}/sub-categories")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get all subcategories by category id",
      tags = {"Categories APIs"})
  public BaseResponse<List<SubCategoryResponse>> getAllSubCategoriesByCategoryId(
      @PathVariable Long categoryId) {
    return this.categoryFacade.getAllSubCategoriesByCategoryId(categoryId);
  }
}
