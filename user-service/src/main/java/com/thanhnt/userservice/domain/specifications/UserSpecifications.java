package com.thanhnt.userservice.domain.specifications;

import com.thanhnt.userservice.domain.entity.users.Users;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
  public static Specification<Users> baseSpecification() {
    return (root, query, criteriaBuilder) -> {
      query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
      return criteriaBuilder.conjunction(); // Return a valid Predicate
    };
  }

  public static Specification<Users> search(String search) {
    return (root, query, criteriaBuilder) -> {
      if (search == null || search.isEmpty()) return null;
      return criteriaBuilder.like(
          criteriaBuilder.upper(root.get("email")), "%" + search.toUpperCase() + "%");
    };
  }

  public static Specification<Users> filterByPhone(String phone) {
    return (root, query, criteriaBuilder) -> {
      if (phone == null || phone.isEmpty()) return null;
      return criteriaBuilder.equal(root.get("phone"), phone);
    };
  }
}
