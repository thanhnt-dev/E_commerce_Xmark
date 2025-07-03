package com.thanhnt.userservice.domain.entity.pointtransaction;

import com.thanhnt.userservice.domain.entity.commons.BaseEntity;
import com.thanhnt.userservice.domain.entity.users.Users;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point_transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PointTransaction extends BaseEntity implements Serializable {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private Users user;

  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @Column(name = "point", nullable = false)
  private Integer point;

  @Column(name = "description")
  private String description;

  @Column(name = "transaction_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;
}
