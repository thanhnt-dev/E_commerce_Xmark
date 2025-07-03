package com.thanhnt.productservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BaseResponse<T> {
  private T metadata;
  private boolean isStatus;

  public static <T> BaseResponse<T> build(T data, boolean isStatus) {
    return (BaseResponse<T>) BaseResponse.builder().isStatus(isStatus).metadata(data).build();
  }

  public static <T> BaseResponse<T> ok() {
    return (BaseResponse<T>) BaseResponse.builder().isStatus(true).metadata("Success").build();
  }

  public static <T> BaseResponse<T> fail(T data) {
    return (BaseResponse<T>) BaseResponse.builder().isStatus(false).metadata(data).build();
  }
}
