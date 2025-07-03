package com.thanhnt.userservice.domain.entity.commons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  INVALIDATE("1000", "Lỗi xác thực"),
  USER_NOT_FOUND("1001", "Không tìm thấy người dùng với email này"),

  // Role exception
  ROLE_NOT_FOUND("1002", "Không tìm thấy vai trò"),

  // sign up
  PHONE_AND_MAIL_EXIST("1050", "Số điện thoại và email đã tồn tại. Vui lòng nhập thông tin khác"),
  EMAIL_EXIST("1051", "Email đã tồn tại! Vui lòng nhập thông tin khác."),
  PHONE_EXIST("1052", "Số điện thoại đã tồn tại! Vui lòng nhập thông tin khác."),

  // login
  ACCOUNT_IS_DEACTIVATED("1100", "Tài khoản của bạn đã bị vô hiệu hóa"),
  ACCOUNT_NON_VERIFY("1101", "Tài khoản của bạn chưa được xác minh. Vui lòng kiểm tra email"),
  BAD_CREDENTIAL_LOGIN("1003", "Tên đăng nhập hoặc mật khẩu không chính xác"),

  // OTP Exception
  OTP_INVALID_OR_EXPIRED("1150", "OTP không hợp lệ hoặc đã hết hạn"),
  OTP_DOES_NOT_MATCH("1151", "OTP không khớp. Vui lòng kiểm tra lại"),

  // Password Exception
  CURRENT_PASSWORD_DOES_NOT_MATCH("1200", "Mật khẩu hiện tại không chính xác"),
  INVALID_CONFIRM_NEW_PASSWORD("1201", "Mật khẩu mới và xác nhận mật khẩu không khớp"),

  // Token Exception
  TOKEN_INVALID_OR_EXPIRED("1250", "Token không hợp lệ hoặc đã hết hạn"),
  TOKEN_DOES_NOT_MATCH("1251", "Token không khớp"),

  // Address Exception
  ADDRESS_NOT_FOUND("1300", "Không tìm thấy địa chỉ"),

  // OWNER Exception
  OWNER_NOT_FOUND("1400", "Không tìm thấy chủ sở hữu");

  private final String code;
  private final String message;
}
