package com.thanhnt.messageservice.api.response;

import com.thanhnt.messageservice.domain.entity.common.ParticipantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
  private Long messageId;
  private Long senderId;
  private ParticipantType senderType;
  private String senderFullName;
  private String senderAvatar;
  private Long recipientId;
  private ParticipantType recipientType;
  private String recipientFullName;
  private String recipientAvatar;
  private String content;
  private Boolean isImage;
}
