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
public class MessageContentResponse {
  private Long messageId;
  private String content;
  private Boolean isImage;

  @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
  private LocalDateTime timeAt;
}
