package com.thanhnt.orderservice.application.service;

import com.thanhnt.orderservice.domain.entity.vouchers.Voucher;

public interface VoucherService {
  Voucher getVoucherById(Long voucherId);

  Voucher getVoucherByCode(String voucherCode);

  void saveVoucher(Voucher voucher);
}
