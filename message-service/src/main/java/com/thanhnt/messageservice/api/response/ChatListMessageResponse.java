package com.thanhnt.messageservice.api.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thanhnt.messageservice.application.helper.CustomLocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatListMessageResponse {
  private Long receiverId;
  private String receiverName;
  private String receiverAvatar;
  private String roomId;
  private String latestMessage;

  @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
  private LocalDateTime latestMessageAt;
}
