package com.thanhnt.productservice.domain.entity.category;

import com.thanhnt.productservice.domain.entity.common.BaseEntity;
import com.thanhnt.productservice.domain.entity.subcategory.SubCategory;
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
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends BaseEntity implements Serializable {
  @Column(name = "category_name", nullable = false)
  private String categoryName;

  @Column(name = "description")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Category> children = new ArrayList<>();

  @OneToMany(
      mappedBy = "category",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @Builder.Default
  private List<SubCategory> subCategories = new ArrayList<>();

  public void updateCategory(String name, String description, Category parent) {
    if (name != null) {
      this.categoryName = name;
    }
    if (description != null) {
      this.description = description;
    }
    if (parent != null) {
      this.parent = parent;
    }
  }
}
