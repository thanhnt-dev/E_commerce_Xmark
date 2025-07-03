package com.thanhnt.orderservice.infrastructure.service;

import com.thanhnt.orderservice.application.exception.AppException;
import com.thanhnt.orderservice.application.service.VoucherService;
import com.thanhnt.orderservice.domain.entity.common.ErrorCode;
import com.thanhnt.orderservice.domain.entity.vouchers.Voucher;
import com.thanhnt.orderservice.domain.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
  private final VoucherRepository voucherRepository;

  @Override
  public Voucher getVoucherById(Long voucherId) {
    return voucherRepository
        .findById(voucherId)
        .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
  }

  @Override
  public Voucher getVoucherByCode(String voucherCode) {
    return voucherRepository.findByVoucherCode(voucherCode);
  }

  @Override
  @Transactional
  public void saveVoucher(Voucher voucher) {
    voucherRepository.save(voucher);
  }
}
