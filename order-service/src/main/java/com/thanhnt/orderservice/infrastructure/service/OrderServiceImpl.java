package com.thanhnt.orderservice.infrastructure.service;

import com.thanhnt.orderservice.api.request.OrderCriteria;
import com.thanhnt.orderservice.application.dto.AdminOrderStatsDTO;
import com.thanhnt.orderservice.application.exception.AppException;
import com.thanhnt.orderservice.application.service.OrderService;
import com.thanhnt.orderservice.domain.entity.common.ErrorCode;
import com.thanhnt.orderservice.domain.entity.orders.Order;
import com.thanhnt.orderservice.domain.repository.OrderRepository;
import com.thanhnt.orderservice.domain.specifications.OrderSpecifications;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;

  @Override
  @Transactional
  public void saveOrder(Order order) {
    orderRepository.save(order);
  }

  @Override
  public Page<Order> findByFilter(OrderCriteria criteria, Long shopId) {
    Pageable pageable =
        PageRequest.of(Math.max(criteria.getCurrentPage() - 1, 0), criteria.getPageSize());
    Specification<Order> specification = OrderSpecifications.baseSpecification();
    if (criteria.getOrderStatus() != null) {
      specification =
          specification.and(OrderSpecifications.filterByStatus(criteria.getOrderStatus()));
    }
    if (criteria.getSearch() != null) {
      specification = specification.and(OrderSpecifications.findBySearch(criteria.getSearch()));
    }
    if (criteria.getSort() != null) {
      specification =
          specification.and(OrderSpecifications.findBySort(criteria.getSort().split("\\.")));
    }
    if (shopId != null) {
      specification = specification.and(OrderSpecifications.filterByShopId(shopId));
    }

    return orderRepository.findAll(specification, pageable);
  }

  @Override
  public Page<Order> findByFilterCustomer(OrderCriteria criteria, Long customerId) {
    Pageable pageable =
        PageRequest.of(Math.max(criteria.getCurrentPage() - 1, 0), criteria.getPageSize());
    Specification<Order> specification = OrderSpecifications.baseSpecification();
    if (criteria.getOrderStatus() != null) {
      specification =
          specification.and(OrderSpecifications.filterByStatus(criteria.getOrderStatus()));
    }
    if (criteria.getSearch() != null) {
      specification = specification.and(OrderSpecifications.findBySearch(criteria.getSearch()));
    }
    if (criteria.getSort() != null) {
      specification =
          specification.and(OrderSpecifications.findBySort(criteria.getSort().split("\\.")));
    }
    if (customerId != null) {
      specification = specification.and(OrderSpecifications.filterByCustomerId(customerId));
    }
    return orderRepository.findAll(specification, pageable);
  }

  @Override
  public Order findByOrderCode(String orderCode) {
    return orderRepository
        .findByOrderCode(orderCode)
        .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
  }

  @Override
  public AdminOrderStatsDTO getOrderStatsByAdmin() {
    Long now = System.currentTimeMillis();
    ZoneId zone = ZoneId.systemDefault();

    // Time ranges
    LocalDateTime todayStart = LocalDate.now().atStartOfDay();
    LocalDateTime weekStart = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
    LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
    LocalDateTime yearStart = LocalDate.now().withDayOfYear(1).atStartOfDay();

    LocalDateTime lastMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
    LocalDateTime lastMonthEnd =
        lastMonthStart.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

    Long todayStartMs = todayStart.atZone(zone).toInstant().toEpochMilli();
    Long weekStartMs = weekStart.atZone(zone).toInstant().toEpochMilli();
    Long monthStartMs = monthStart.atZone(zone).toInstant().toEpochMilli();
    Long yearStartMs = yearStart.atZone(zone).toInstant().toEpochMilli();

    Long lastMonthStartMs = lastMonthStart.atZone(zone).toInstant().toEpochMilli();
    Long lastMonthEndMs = lastMonthEnd.atZone(zone).toInstant().toEpochMilli();

    Long totalOrders = orderRepository.count();
    Long totalToday = orderRepository.countByCreatedAtBetween(todayStartMs, now);
    Long totalThisWeek = orderRepository.countByCreatedAtBetween(weekStartMs, now);
    Long totalThisMonth = orderRepository.countByCreatedAtBetween(monthStartMs, now);
    Long totalThisYear = orderRepository.countByCreatedAtBetween(yearStartMs, now);
    Long totalLastMonth = orderRepository.countByCreatedAtBetween(lastMonthStartMs, lastMonthEndMs);

    BigDecimal totalRevenue = orderRepository.getTotalRevenue();
    BigDecimal avgRevenue = orderRepository.getAverageOrderAmount();

    double percentageChange = 0.0;
    if (totalLastMonth != 0) {
      percentageChange = ((double) (totalThisMonth - totalLastMonth) / totalLastMonth) * 100;
    }

    return AdminOrderStatsDTO.builder()
        .totalOrders(totalOrders)
        .totalOrdersToday(totalToday)
        .totalOrdersThisWeek(totalThisWeek)
        .totalOrdersThisMonth(totalThisMonth)
        .totalOrdersThisYear(totalThisYear)
        .totalOrdersLastMonth(totalLastMonth)
        .totalPrice(totalRevenue != null ? totalRevenue.longValue() : 0L)
        .averagePrice(avgRevenue != null ? avgRevenue.doubleValue() : 0.0)
        .percentageChangeFromLastMonth(percentageChange)
        .build();
  }
}
