package com.thanhnt.messageservice.domain.entity.notifications;

import com.thanhnt.messageservice.domain.entity.common.BaseEntity;
import com.thanhnt.messageservice.domain.entity.common.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity implements Serializable {

  @Column(name = "sender_id", nullable = false)
  private Long senderId;

  @Column(name = "sender_name")
  private String senderName;

  @Column(name = "content")
  private String content;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "is_read", nullable = false)
  @Builder.Default
  private boolean read = false;

  @Column(name = "notification_type", nullable = false)
  private NotificationType notificationType;

  public void isNotificationRead() {
    this.read = true;
  }
}
