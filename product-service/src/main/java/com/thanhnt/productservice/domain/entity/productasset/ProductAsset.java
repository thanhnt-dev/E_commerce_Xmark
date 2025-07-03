package com.thanhnt.productservice.domain.entity.productasset;

import com.thanhnt.productservice.domain.entity.common.BaseEntity;
import com.thanhnt.productservice.domain.entity.product.Product;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "product_assets")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAsset extends BaseEntity implements Serializable {
  @Column(name = "media_key", nullable = false)
  private String mediaKey;

  @Column(name = "public_id", nullable = false)
  private String publicId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  public void updateImage(String mediaKey, String publicId) {
    this.mediaKey = mediaKey;
    this.publicId = publicId;
  }
}
