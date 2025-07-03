package com.thanhnt.orderservice.domain.specifications;

import com.thanhnt.orderservice.domain.entity.common.OrderStatus;
import com.thanhnt.orderservice.domain.entity.orders.Order;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecifications {
  public static Specification<Order> baseSpecification() {
    return (root, query, criteriaBuilder) -> {
      if (query != null) {
        query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
      }
      return criteriaBuilder.conjunction();
    };
  }

  public static Specification<Order> filterByStatus(OrderStatus status) {
    return (root, query, criteriaBuilder) -> {
      if (status == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("status"), status);
    };
  }

  public static Specification<Order> findBySearch(String search) {
    return (root, query, criteriaBuilder) -> {
      if (search == null || search.trim().isEmpty()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get("orderCode")), "%" + search.toLowerCase() + "%");
    };
  }

  public static Specification<Order> findBySort(String[] sort) {
    return (root, query, criteriaBuilder) -> {
      if (sort == null || sort.length != 2) {
        return criteriaBuilder.conjunction();
      }

      String field = sort[0].trim();
      String direction = sort[1].trim().toLowerCase();

      if (!"createdAt".equals(field)) {
        return criteriaBuilder.conjunction();
      }

      if ("asc".equals(direction)) {
        query.orderBy(criteriaBuilder.asc(root.get("createdAt")));
      } else if ("desc".equals(direction)) {
        query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
      }

      return criteriaBuilder.conjunction();
    };
  }

  public static Specification<Order> filterByShopId(Long shopId) {
    return (root, query, criteriaBuilder) -> {
      if (shopId == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("shopId"), shopId);
    };
  }

  public static Specification<Order> filterByCustomerId(Long customerId) {
    return (root, query, criteriaBuilder) -> {
      if (customerId == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("userId"), customerId);
    };
  }
}
