package com.thanhnt.orderservice.domain.entity.orders;

import com.thanhnt.orderservice.domain.entity.common.BaseEntity;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem extends BaseEntity implements Serializable {

  @Column(name = "product_variant_id", nullable = false)
  private Long productId;

  @Column(name = "product_variant_name", length = 100, nullable = false)
  private String productName;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "image_url", length = 255)
  private String imageUrl;

  @Column(name = "price_unit", nullable = false)
  private BigDecimal priceUnit;

  @JoinColumn(name = "order_id", nullable = false)
  @ManyToOne
  private Order order;
}
