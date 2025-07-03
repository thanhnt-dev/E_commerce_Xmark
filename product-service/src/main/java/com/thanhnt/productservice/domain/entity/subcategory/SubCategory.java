package com.thanhnt.productservice.domain.entity.subcategory;

import com.thanhnt.productservice.domain.entity.category.Category;
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
@Table(name = "sub_categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SubCategory extends BaseEntity implements Serializable {

  @Column(name = "sub_category_name", nullable = false)
  private String subCategoryName;

  @Column(name = "description")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Product> products = new ArrayList<>();

  public void updateSubCategory(String subCategoryName, String description, Category category) {
    this.subCategoryName = subCategoryName;
    this.description = description;
    this.category = category;
  }
}
