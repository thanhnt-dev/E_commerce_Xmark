package com.thanhnt.productservice.application.dto;

import com.thanhnt.productservice.domain.entity.common.NotificationType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NotificationDTO {
  private Long senderId;
  private String senderName;
  private Long receiverId;
  private String receiverName;
  private String content;
  private NotificationType type;
  private boolean isRead;
  private Long createdAt;
}
