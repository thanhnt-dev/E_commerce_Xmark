package com.thanhnt.messageservice.domain.specification;

import com.thanhnt.messageservice.domain.entity.message.Message;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class MessageSpecification {
  public static Specification<Message> baseSpecification(Long senderId, Long receiverId) {
    return (root, query, criteriaBuilder) -> {
      Predicate predicate1 = criteriaBuilder.equal(root.get("senderId"), senderId);
      Predicate predicate2 = criteriaBuilder.equal(root.get("receiverId"), receiverId);
      Predicate predicate3 = criteriaBuilder.equal(root.get("senderId"), receiverId);
      Predicate predicate4 = criteriaBuilder.equal(root.get("receiverId"), senderId);
      Predicate predicate5 = criteriaBuilder.and(predicate1, predicate2);
      Predicate predicate6 = criteriaBuilder.and(predicate3, predicate4);
      query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
      return criteriaBuilder.or(predicate5, predicate6);
    };
  }
}
