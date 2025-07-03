package com.thanhnt.orderservice.domain.repository;

import com.thanhnt.orderservice.domain.entity.vouchers.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
  Voucher findByVoucherCode(String voucherCode);
}
