package com.thanhnt.orderservice.application.service;

import com.thanhnt.orderservice.api.request.OrderCriteria;
import com.thanhnt.orderservice.application.dto.AdminOrderStatsDTO;
import com.thanhnt.orderservice.domain.entity.orders.Order;
import org.springframework.data.domain.Page;

public interface OrderService {
  void saveOrder(Order order);

  Page<Order> findByFilter(OrderCriteria criteria, Long shopId);

  Page<Order> findByFilterCustomer(OrderCriteria criteria, Long customerId);

  Order findByOrderCode(String orderCode);

  AdminOrderStatsDTO getOrderStatsByAdmin();
}
