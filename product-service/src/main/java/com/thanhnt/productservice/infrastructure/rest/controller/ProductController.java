package com.thanhnt.productservice.infrastructure.rest.controller;

import com.thanhnt.productservice.api.facade.ProductFacade;
import com.thanhnt.productservice.api.request.*;
import com.thanhnt.productservice.api.response.BaseResponse;
import com.thanhnt.productservice.api.response.PaginationResponse;
import com.thanhnt.productservice.api.response.ProductDetailResponse;
import com.thanhnt.productservice.api.response.ProductResponse;
import com.thanhnt.productservice.application.dto.ProductValidDTO;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductFacade productFacade;

  @PostMapping("/upsert")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_STORE_OWNER')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Upsert product",
      tags = {"Products APIs"})
  public BaseResponse<Long> upsertProduct(@RequestBody @Validated UpsertProductRequest request) {
    return this.productFacade.upsertProduct(request);
  }

  @PutMapping("/product-variant/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_STORE_OWNER')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Update product variants",
      tags = {"Products APIs"})
  public BaseResponse<Long> upsertProductVariant(
      @PathVariable Long id, @RequestBody @Validated UpsertProductVariantRequest request) {
    return this.productFacade.updateProductVariant(id, request);
  }

  @PostMapping(value = "/{id}/upload_images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('ROLE_STORE_OWNER')")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Upload multiple images for a product",
      tags = {"Products APIs"})
  public BaseResponse<Void> uploadMultipleProductImages(
      @Parameter(
              name = "images",
              description = "List of images to upload",
              required = true,
              content =
                  @Content(
                      mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                      array = @ArraySchema(schema = @Schema(type = "string", format = "binary"))))
          @RequestPart
          List<MultipartFile> images,
      @RequestParam Long id)
      throws IOException {
    return this.productFacade.uploadMultipleProductImages(images, id);
  }

  @PutMapping(value = "/upload_image/{imageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('ROLE_STORE_OWNER')")
  @SecurityRequirement(name = "Bearer Authentication")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = "Update multiple images of Id Image",
      tags = {"Products APIs"})
  BaseResponse<String> updateImageForProductWithId(
      @RequestPart MultipartFile file, @PathVariable Long imageId) throws IOException {
    return this.productFacade.uploadProductImageWithImageId(file, imageId);
  }

  @PutMapping("/product-variant/{id}/update_status")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_STORE_OWNER')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Update status active product variant",
      tags = {"Products APIs"})
  public BaseResponse<Void> updateStatusProductVariant(
      @PathVariable Long id, @RequestParam boolean isActive) {
    return this.productFacade.updateStatusProductVariant(id, isActive);
  }

  @PutMapping("/{id}/update_status")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_STORE_OWNER')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Update status active product",
      tags = {"Products APIs"})
  public BaseResponse<Void> updateStatusProduct(
      @PathVariable Long id, @RequestParam boolean isActive) {
    return this.productFacade.updateStatusProduct(id, isActive);
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get Products with filter",
      tags = {"Products APIs"})
  public BaseResponse<PaginationResponse<List<ProductResponse>>> getProductWithFilter(
      ProductCriteria criteria) {
    return this.productFacade.getProductsWithFilter(criteria, false);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get Products Detail by Id",
      tags = {"Products APIs"})
  public BaseResponse<ProductDetailResponse> getProductDetailById(@PathVariable Long id) {
    return this.productFacade.getProductDetailBy(id);
  }

  @GetMapping("/admin")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get Products by filter for admin",
      tags = {"Products APIs"})
  public BaseResponse<PaginationResponse<List<ProductResponse>>> getProductWithFilterByAdmin(
      ProductCriteria criteria) {
    return this.productFacade.getProductsWithFilter(criteria, true);
  }

  @GetMapping("/owner")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_STORE_OWNER')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Get Products by filter for owner",
      tags = {"Products APIs"})
  public BaseResponse<PaginationResponse<List<ProductResponse>>> getProductWithFilterByOwner(
      ProductCriteria criteria) {
    return this.productFacade.getProductsByOwner(criteria);
  }

  @PutMapping("/admin/approve/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Approve product by admin",
      tags = {"Products APIs"})
  public BaseResponse<Void> updateApproveByAdmin(
      @PathVariable Long id, @RequestBody ProductApprovalRequest request) {

    return this.productFacade.approveProduct(id, request);
  }

  @PostMapping("/sync")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @SecurityRequirement(name = "Bearer Authentication")
  @Operation(
      summary = "Sync manual product by Id",
      tags = {"Products APIs"})
  public BaseResponse<Void> syncManualProduct(@RequestBody SyncProductRequest request) {
    return this.productFacade.syncManualProduct(request);
  }

  @Hidden
  @PostMapping(value = "/validate", headers = "API_SECRET_HEADER=secret1403")
  @Operation(
      summary = "Validate products",
      tags = {"Products APIs"})
  public ResponseEntity<List<ProductValidDTO>> validateProducts(
      @RequestBody List<ValidateOrderItemRequest> requests) {
    return this.productFacade.validateProducts(requests);
  }

  @Hidden
  @PostMapping(value = "/update_quantity", headers = "API_SECRET_HEADER=secret1403")
  @Operation(
      summary = "Update quantity products",
      tags = {"Products APIs"})
  public ResponseEntity<Boolean> updateProductQuantity(
      @RequestBody List<ValidateOrderItemRequest> requests) {
    return this.productFacade.updateProductQuantity(requests);
  }
}
