package com.thanhnt.storeservice.domain.entity.shopsubscription;

import com.thanhnt.storeservice.domain.entity.common.BaseEntity;
import com.thanhnt.storeservice.domain.entity.common.SubscriptionStatus;
import com.thanhnt.storeservice.domain.entity.shop.Shop;
import com.thanhnt.storeservice.domain.entity.subscriptionplan.SubscriptionPlan;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shop_subscriptions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopSubscription extends BaseEntity implements Serializable {

  @Column(name = "expired_at", nullable = false)
  private Long expiredAt;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private SubscriptionStatus status;

  @ManyToOne
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;

  @ManyToOne
  @JoinColumn(name = "subscription_id", nullable = false)
  private SubscriptionPlan subscription;
}
