package com.thanhnt.productservice.domain.entity.productvariant;

import com.thanhnt.productservice.domain.entity.common.BaseEntity;
import com.thanhnt.productservice.domain.entity.common.ProductCondition;
import com.thanhnt.productservice.domain.entity.product.Product;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_variants")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ProductVariant extends BaseEntity implements Serializable {
  @Column(name = "product_size")
  private String productSize;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "original_price", nullable = false)
  private Long originalPrice;

  @Column(name = "resale_price", nullable = false)
  private Long resalePrice;

  @Column(name = "condition")
  @Enumerated(EnumType.STRING)
  private ProductCondition condition;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  public void updateProductVariant(
      String productSize,
      Integer quantity,
      Long originalPrice,
      Long resalePrice,
      ProductCondition condition) {
    if (productSize != null) {
      this.productSize = productSize;
    }
    if (quantity != null) {
      this.quantity = quantity;
    }
    if (originalPrice != null) {
      this.originalPrice = originalPrice;
    }
    if (resalePrice != null) {
      this.resalePrice = resalePrice;
    }
    if (condition != null) {
      this.condition = condition;
    }
  }

  public void updateQuantity(Integer quantity) {
    if (quantity != null) {
      this.quantity = quantity;
    }
  }
}
