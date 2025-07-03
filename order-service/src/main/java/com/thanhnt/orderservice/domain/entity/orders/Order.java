package com.thanhnt.orderservice.domain.entity.orders;

import com.thanhnt.orderservice.domain.entity.common.BaseEntity;
import com.thanhnt.orderservice.domain.entity.common.OrderStatus;
import com.thanhnt.orderservice.domain.entity.common.PaymentType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity implements Serializable {

  @Column(name = "order_code", length = 40, nullable = false, unique = true)
  private String orderCode;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "shop_id", nullable = false)
  private Long shopId;

  @Column(name = "phone", length = 50)
  private String phone;

  @Column(name = "name", length = 50)
  private String name;

  @Column(name = "address", length = 100)
  private String address;

  @Column(name = "note")
  private String note;

  @Column(name = "delivery_id")
  private Long deliveryId;

  @Column(name = "payment_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentType paymentType;

  @Column(name = "total_price")
  private BigDecimal totalPrice;

  @Column(name = "shipping_fee")
  @Builder.Default
  private BigDecimal shippingFee = BigDecimal.ZERO;

  @Column(name = "discount_total")
  @Builder.Default
  private BigDecimal discountTotal = BigDecimal.ZERO;

  @Column(name = "total_amount", nullable = false)
  @Builder.Default
  private BigDecimal totalAmount = BigDecimal.ZERO;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private OrderStatus status = OrderStatus.PENDING;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<OrderVoucher> orderVouchers = new ArrayList<>();

  public void updateStatus(OrderStatus status) {
    this.status = status;
  }

  public void updateOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }
}
