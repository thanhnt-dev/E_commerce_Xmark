package com.thanhnt.orderservice.domain.entity.vouchers;

import com.thanhnt.orderservice.domain.entity.common.BaseEntity;
import com.thanhnt.orderservice.domain.entity.common.DiscountType;
import com.thanhnt.orderservice.domain.entity.orders.OrderVoucher;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "vouchers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Voucher extends BaseEntity implements Serializable {

  @Column(name = "code", nullable = false, unique = true, length = 20)
  private String voucherCode;

  @Column(name = "discount_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private DiscountType discountType;

  @Column(name = "discount_value", nullable = false)
  private Integer discountValue;

  @Column(name = "min_order_value", nullable = false)
  @Builder.Default
  private Integer minOrderValue = 0;

  @Column(name = "max_discount")
  private Integer maxDiscount;

  @Column(name = "start_date", nullable = false)
  private Long startDate;

  @Column(name = "end_date", nullable = false)
  private Long endDate;

  @Column(name = "usage_limit", nullable = false)
  @Builder.Default
  private Integer usageLimit = 1;

  @OneToMany(
      mappedBy = "voucher",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @Builder.Default
  private List<OrderVoucher> orderVouchers = new ArrayList<>();

  @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private List<UserVoucher> userVouchers = new ArrayList<>();

  public void updateUsageLimit() {
    if (usageLimit != null && usageLimit >= 0) {
      this.usageLimit -= 1;
    }
  }
}
