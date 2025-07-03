package com.thanhnt.orderservice.infrastructure.service;

import com.thanhnt.orderservice.application.exception.AppException;
import com.thanhnt.orderservice.application.service.VoucherUserService;
import com.thanhnt.orderservice.domain.entity.common.ErrorCode;
import com.thanhnt.orderservice.domain.entity.vouchers.UserVoucher;
import com.thanhnt.orderservice.domain.repository.VoucherUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VoucherUserServiceImpl implements VoucherUserService {
  private final VoucherUserRepository voucherUserRepository;

  @Override
  public UserVoucher getUserVoucherById(Long userVoucherId) {
    return voucherUserRepository
        .findById(userVoucherId)
        .orElseThrow(() -> new AppException(ErrorCode.USER_VOUCHER_NOT_FOUND));
  }

  @Override
  public UserVoucher getUserVoucherByUserIdAndVoucherId(Long userId, Long voucherId) {
    return voucherUserRepository.findByUserIdAndVoucherId(userId, voucherId);
  }

  @Override
  @Transactional
  public UserVoucher saveUserVoucher(UserVoucher userVoucher) {
    return voucherUserRepository.save(userVoucher);
  }
}
