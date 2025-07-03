package com.thanhnt.productservice.domain.entity.brand;

import com.thanhnt.productservice.domain.entity.common.BaseEntity;
import com.thanhnt.productservice.domain.entity.product.Product;
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
@Table(name = "brands")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Brand extends BaseEntity implements Serializable {

  @Column(name = "brand_name", nullable = false)
  private String brandName;

  @Column(name = "description")
  private String description;

  @Column(name = "logo_url")
  private String logoUrl;

  @OneToMany(
      mappedBy = "brand",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @Builder.Default
  private List<Product> products = new ArrayList<>();
}
