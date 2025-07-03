package com.thanhnt.orderservice.domain.entity.vouchers;

import com.thanhnt.orderservice.domain.entity.common.BaseEntity;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_vouchers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVoucher extends BaseEntity implements Serializable {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "voucher_id", nullable = false)
  private Voucher voucher;

  @Column(name = "is_used", nullable = false)
  @Builder.Default
  private Boolean isUsed = false;

  @Column(name = "used_at")
  private Long usedAt;
}
