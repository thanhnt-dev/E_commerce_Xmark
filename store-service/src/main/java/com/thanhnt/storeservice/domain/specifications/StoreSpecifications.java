package com.thanhnt.storeservice.domain.specifications;

import com.thanhnt.storeservice.domain.entity.common.BusinessType;
import com.thanhnt.storeservice.domain.entity.common.VerificationStatus;
import com.thanhnt.storeservice.domain.entity.shop.Shop;
import org.springframework.data.jpa.domain.Specification;

public class StoreSpecifications {

  public static Specification<Shop> baseSpecification() {
    return (root, query, criteriaBuilder) -> {
      query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
      return criteriaBuilder.conjunction(); // Return a valid Predicate
    };
  }

  public static Specification<Shop> search(String search) {
    return (root, query, criteriaBuilder) -> {
      if (search == null || search.isEmpty()) return null;
      return criteriaBuilder.like(
          criteriaBuilder.upper(root.get("name")), "%" + search.toLowerCase() + "%");
    };
  }

  public static Specification<Shop> businessType(BusinessType businessType) {
    return (root, query, criteriaBuilder) -> {
      if (businessType == null) return null;
      return criteriaBuilder.equal(root.get("businessType"), businessType);
    };
  }

  public static Specification<Shop> verificationStatus(VerificationStatus verificationStatus) {
    return (root, query, criteriaBuilder) -> {
      if (verificationStatus == null) return null;
      return criteriaBuilder.equal(root.get("verificationStatus"), verificationStatus);
    };
  }

  public static Specification<Shop> sort(String sort) {
    if (sort == null || sort.isEmpty()) return null;
    return (root, query, criteriaBuilder) -> {
      if (sort.equals("asc")) {
        query.orderBy(criteriaBuilder.asc(root.get("createdAt")));
      } else {
        query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
      }
      return criteriaBuilder.conjunction();
    };
  }

  public static Specification<Shop> filterByCreatedAt(Long startDate, Long endDate) {
    return (root, query, criteriaBuilder) -> {
      if (startDate == null && endDate == null) return null;
      if (startDate == null) {
        return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
      }
      if (endDate == null) {
        return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
      }
      return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
    };
  }
}
