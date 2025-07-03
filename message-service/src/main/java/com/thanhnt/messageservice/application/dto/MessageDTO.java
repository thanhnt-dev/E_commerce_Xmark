package com.thanhnt.messageservice.application.dto;

import com.thanhnt.messageservice.domain.entity.common.ParticipantType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDTO {
  private Long senderId;
  private ParticipantType senderType;
  private Long receiverId;
  private ParticipantType receiverType;
  private String content;
  private String imageUrl;
  private String type;
  private Boolean isImage;
}
