package com.thanhnt.storeservice.domain.entity.shopproduct;

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
@Table(name = "shop_products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopProducts extends BaseEntity implements Serializable {

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @ManyToOne
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;
}
