package com.thanhnt.messageservice.api.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thanhnt.messageservice.application.helper.CustomLocalDateTimeSerializer;
import com.thanhnt.messageservice.domain.entity.common.NotificationType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
  private Long id;
  private Long senderId;
  private String senderName;
  private Long receiverId;
  private String receiverName;
  private String content;
  private NotificationType type;
  private boolean isRead;

  @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
  private LocalDateTime createdAt;
}
