package com.thanhnt.productservice.api.facade;

import com.thanhnt.productservice.api.request.*;
import com.thanhnt.productservice.api.response.BaseResponse;
import com.thanhnt.productservice.api.response.PaginationResponse;
import com.thanhnt.productservice.api.response.ProductDetailResponse;
import com.thanhnt.productservice.api.response.ProductResponse;
import com.thanhnt.productservice.application.dto.ProductValidDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProductFacade {
  BaseResponse<Long> upsertProduct(UpsertProductRequest request);

  BaseResponse<Long> updateProductVariant(Long id, UpsertProductVariantRequest request);

  BaseResponse<Void> uploadMultipleProductImages(List<MultipartFile> multipartFiles, Long productId)
      throws IOException;

  BaseResponse<String> uploadProductImageWithImageId(MultipartFile multipartFile, Long imageId)
      throws IOException;

  BaseResponse<Void> updateStatusProductVariant(Long id, boolean isActive);

  BaseResponse<Void> updateStatusProduct(Long id, boolean isActive);

  BaseResponse<PaginationResponse<List<ProductResponse>>> getProductsWithFilter(
      ProductCriteria criteria, boolean isAdmin);

  BaseResponse<PaginationResponse<List<ProductResponse>>> getProductsByOwner(
      ProductCriteria criteria);

  BaseResponse<ProductDetailResponse> getProductDetailBy(Long id);

  BaseResponse<Void> approveProduct(Long id, ProductApprovalRequest request);

  BaseResponse<Void> syncManualProduct(SyncProductRequest request);

  ResponseEntity<List<ProductValidDTO>> validateProducts(List<ValidateOrderItemRequest> request);

  ResponseEntity<Boolean> updateProductQuantity(List<ValidateOrderItemRequest> request);
}
