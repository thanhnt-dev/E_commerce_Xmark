package com.thanhnt.orderservice.domain.repository;

import com.thanhnt.orderservice.domain.entity.orders.Order;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository
    extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

  Optional<Order> findByOrderCode(String orderCode);

  Long countByCreatedAtBetween(Long start, Long end);

  @Query("SELECT SUM(o.totalAmount) FROM Order o")
  BigDecimal getTotalRevenue();

  @Query("SELECT AVG(o.totalAmount) FROM Order o")
  BigDecimal getAverageOrderAmount();
}
