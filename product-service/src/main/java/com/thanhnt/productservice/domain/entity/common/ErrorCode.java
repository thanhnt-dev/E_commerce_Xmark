package com.thanhnt.productservice.domain.entity.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  INVALIDATE("1000", "Lỗi xác thực"),

  // UNAUTHORIZED
  API_KEY_INVALID("1050", "API key không hợp lệ."),

  // CATEGORY
  CATEGORY_NOT_FOUND("1100", "Không tìm thấy danh mục."),
  SUBCATEGORY_NOT_FOUND("1101", "Không tìm thấy danh mục con."),

  // PRODUCT
  PRODUCT_CONVERT_ERROR("2101", "Không thể chuyển đổi giữa chuỗi và HashMap."),
  PRODUCT_NOT_FOUND("2102", "Không tìm thấy sản phẩm."),
  PRODUCT_VARIANT_NOT_FOUND("2103", "Không tìm thấy biến thể sản phẩm."),
  PRODUCT_ASSET_NOT_FOUND("2104", "Không tìm thấy tài nguyên của sản phẩm."),
  PRODUCT_ID_NOT_FOUND("2105", "Không tìm thấy ID sản phẩm."),

  // BRAND
  BRAND_NOT_FOUND("2200", "Không tìm thấy thương hiệu."),

  // SUBCATEGORY
  SUB_CATEGORY_NOT_FOUND("2300", "Không tìm thấy danh mục con."),

  // STORE
  STORE_NOT_FOUND("3000", "Không tìm thấy cửa hàng.");

  private final String code;
  private final String message;
}
