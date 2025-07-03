package com.thanhnt.orderservice.domain.repository;

import com.thanhnt.orderservice.domain.entity.vouchers.UserVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherUserRepository extends JpaRepository<UserVoucher, Long> {
  UserVoucher findByUserIdAndVoucherId(Long userId, Long voucherId);
}
