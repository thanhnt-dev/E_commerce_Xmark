package com.thanhnt.orderservice.domain.entity.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  INVALIDATE("1000", "Lỗi xác thực"),

  VOUCHER_NOT_FOUND("2000", "Voucher không tồn tại"),
  VOUCHER_EXPIRED("2001", "Voucher đã hết hạn"),
  VOUCHER_NOT_APPLICABLE("2002", "Voucher không áp dụng được cho đơn hàng này"),
  VOUCHER_NOT_STARTED("2003", "Voucher chưa bắt đầu sử dụng"),
  USER_VOUCHER_NOT_FOUND("3000", "User Voucher không tồn tại"),
  ORDER_NOT_FOUND("4000", "Đơn hàng không tồn tại"),
  ORDER_NO_PERMISSION("4001", "Bạn không có quyền truy cập vào đơn hàng này"),
  STORE_NOT_FOUND("5000", "Cửa hàng không tồn tại hoặc bạn không có quyền truy cập"),
  PAYMENT_NOT_SUCCESS("6000", "Thanh toán không thành công"),
  PAYMENT_FAILED("6001", "Lỗi trong quá trình thanh toán");

  private final String code;
  private final String message;
}
