package com.thanhnt.storeservice.domain.entity.shoprequest;

import com.thanhnt.storeservice.domain.entity.common.BaseEntity;
import com.thanhnt.storeservice.domain.entity.shop.Shop;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shop_requests")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopRequests extends BaseEntity implements Serializable {

  @ManyToOne
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;

  @Column(name = "admin_id", nullable = false)
  private Long adminId;

  @Column(name = "reason", nullable = false)
  private String reason;
}
