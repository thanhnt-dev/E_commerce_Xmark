package com.thanhnt.storeservice.domain.entity.subscriptionplan;

import com.thanhnt.storeservice.domain.entity.common.BaseEntity;
import com.thanhnt.storeservice.domain.entity.shopsubscription.ShopSubscription;
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
@Table(name = "subscription_plans")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionPlan extends BaseEntity implements Serializable {

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "price", nullable = false)
  private Long price;

  @Column(name = "duration", nullable = false)
  private Integer duration;

  @Column(name = "priority_level", nullable = false)
  private Integer priorityLevel;

  @OneToMany(
      mappedBy = "subscription",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @Builder.Default
  private List<ShopSubscription> subscriptionPlanFeatures = new ArrayList<>();
}
