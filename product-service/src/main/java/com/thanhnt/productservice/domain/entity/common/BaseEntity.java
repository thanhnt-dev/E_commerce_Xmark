package com.thanhnt.productservice.domain.entity.common;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private boolean isActive = true;

  @Version @Builder.Default private Integer version = 1;

  @Column(name = "created_at", nullable = false)
  @Builder.Default
  private Long createdAt = Instant.now().toEpochMilli();

  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  private Long updatedAt = Instant.now().toEpochMilli();

  @PreUpdate
  public void updateAt() {
    this.updatedAt = Instant.now().toEpochMilli();
  }

  public void updateStatus(boolean status) {
    this.isActive = status;
  }
}
