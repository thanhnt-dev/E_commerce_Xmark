package com.thanhnt.orderservice.api.request;

import com.thanhnt.orderservice.domain.entity.common.OrderStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCriteria {
  private Integer currentPage;
  private Integer pageSize;
  private String search;
  private String sort;
  private OrderStatus orderStatus;
}
