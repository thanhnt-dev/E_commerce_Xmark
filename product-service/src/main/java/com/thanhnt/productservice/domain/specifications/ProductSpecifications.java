package com.thanhnt.productservice.domain.specifications;

import com.thanhnt.productservice.domain.entity.common.ProductApprovalStatus;
import com.thanhnt.productservice.domain.entity.product.Product;
import com.thanhnt.productservice.domain.entity.productvariant.ProductVariant;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
  public static Specification<Product> baseSpecification(boolean isAdmin) {
    return (root, query, criteriaBuilder) -> {
      query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
      if (isAdmin) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("isActive"), true);
    };
  }

  public static Specification<Product> filterByPrice(Long minPrice, Long maxPrice) {
    return (root, query, criteriaBuilder) -> {
      query.distinct(true);

      Join<Product, ProductVariant> variantJoin = root.join("productVariants", JoinType.INNER);

      if (minPrice == null && maxPrice == null) {
        return criteriaBuilder.conjunction();
      }
      if (minPrice == null) {
        return criteriaBuilder.lessThanOrEqualTo(variantJoin.get("resalePrice"), maxPrice);
      }
      if (maxPrice == null) {
        return criteriaBuilder.greaterThanOrEqualTo(variantJoin.get("resalePrice"), minPrice);
      }
      return criteriaBuilder.between(variantJoin.get("resalePrice"), minPrice, maxPrice);
    };
  }

  public static Specification<Product> filterByBrandId(Long brandId) {
    return (root, query, criteriaBuilder) -> {
      if (brandId == null) {
        return null;
      }
      return criteriaBuilder.equal(root.get("brand").get("id"), brandId);
    };
  }

  public static Specification<Product> filterByName(String name) {
    return (root, query, criteriaBuilder) -> {
      if (name == null || name.trim().isEmpty()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get("productName")), "%" + name.toLowerCase() + "%");
    };
  }

  public static Specification<Product> filterByCategory(Long subCategoryId) {
    return (root, query, criteriaBuilder) -> {
      if (subCategoryId == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("subCategory").get("id"), subCategoryId);
    };
  }

  public static Specification<Product> filterByStoreId(Long storeId) {
    return (root, query, criteriaBuilder) -> {
      if (storeId == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("shopId").get("id"), storeId);
    };
  }

  public static Specification<Product> filterByProductApprovalStatus(ProductApprovalStatus status) {
    return (root, query, criteriaBuilder) -> {
      if (status == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("productApprovalStatus"), status);
    };
  }

  public static Specification<Product> sort(String[] sort) {
    return (root, query, criteriaBuilder) -> {
      if (sort == null || sort.length != 2) {
        return null;
      }

      if (sort[0].equals("name")) {
        if (sort[1].equals("asc")) {
          query.orderBy(criteriaBuilder.asc(root.get("productName")));
        }
        if (sort[1].equals("desc")) {
          query.orderBy(criteriaBuilder.desc(root.get("productName")));
        }
      } else {
        if (sort[1].equals("asc")) {
          query.orderBy(criteriaBuilder.asc(root.get("productPrice")));
        }
        if (sort[1].equals("desc")) {
          query.orderBy(criteriaBuilder.desc(root.get("productPrice")));
        }
      }
      return null;
    };
  }
}
