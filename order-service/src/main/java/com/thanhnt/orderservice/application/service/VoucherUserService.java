package com.thanhnt.orderservice.application.service;

import com.thanhnt.orderservice.domain.entity.vouchers.UserVoucher;

public interface VoucherUserService {
  UserVoucher getUserVoucherById(Long userVoucherId);

  UserVoucher getUserVoucherByUserIdAndVoucherId(Long userId, Long voucherId);

  UserVoucher saveUserVoucher(UserVoucher userVoucher);
}
