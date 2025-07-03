package com.thanhnt.messageservice.application.dto;

import com.thanhnt.messageservice.domain.entity.common.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
