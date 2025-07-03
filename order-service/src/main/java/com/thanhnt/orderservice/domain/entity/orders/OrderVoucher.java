package com.thanhnt.orderservice.domain.entity.orders;

import com.thanhnt.orderservice.domain.entity.common.BaseEntity;
import com.thanhnt.orderservice.domain.entity.vouchers.Voucher;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_vouchers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderVoucher extends BaseEntity implements Serializable {

  @JoinColumn(name = "order_id", nullable = false)
  @ManyToOne
  private Order order;

  @JoinColumn(name = "voucher_id", nullable = false)
  @ManyToOne
  private Voucher voucher;

  @Column(name = "discount_amount", nullable = false)
  private Integer discountAmount;
}
