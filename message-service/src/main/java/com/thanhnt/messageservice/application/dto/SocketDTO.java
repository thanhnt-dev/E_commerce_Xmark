package com.thanhnt.messageservice.application.dto;

import com.thanhnt.messageservice.domain.entity.common.SocketType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketDTO<T> {
  private SocketType type;
  private T data;

  public static <T> SocketDTO<T> of(SocketType type, T data) {
    return SocketDTO.<T>builder().type(type).data(data).build();
  }
}
