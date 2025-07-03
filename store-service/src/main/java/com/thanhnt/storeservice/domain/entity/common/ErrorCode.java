package com.thanhnt.storeservice.domain.entity.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  INVALIDATE("1000", "Lỗi xác thực"),

  // UNAUTHORIZED
  API_KEY_INVALID("1050", "API key không hợp lệ."),

  // SHOP EXCEPTION
  INVALID_VERIFY_IDENTITY("1100", "Cần cung cấp số CMND/CCCD để xác minh danh tính."),
  SHOP_ALREADY_REGISTERED("1101", "Mỗi người dùng chỉ được đăng ký một cửa hàng."),
  SHOP_NOT_FOUND("1102", "Không tìm thấy cửa hàng."),
  PHONE_ALREADY_REGISTERED("1103", "Số điện thoại đã được đăng ký cho một cửa hàng."),
  EMAIL_ALREADY_REGISTERED("1104", "Email đã được đăng ký cho một cửa hàng."),
  CATEGORY_REQUIRED("1105", "Phải chọn danh mục khi loại hình kinh doanh là VĂN PHÒNG."),
  SHOP_VERIFIED(
      "1106", "Cửa hàng đã được xác minh. Không thể chỉnh sửa, vui lòng liên hệ quản trị viên."),
  UPDATE_USER_ROLE_FAILED("1107", "Cập nhật vai trò người dùng thất bại.");

  private final String code;
  private final String message;
}
