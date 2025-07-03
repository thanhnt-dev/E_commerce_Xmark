package com.thanhnt.storeservice.domain.entity.shopfollower;

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
@Table(name = "shop_followers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopFollowers extends BaseEntity implements Serializable {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @ManyToOne
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;
}
