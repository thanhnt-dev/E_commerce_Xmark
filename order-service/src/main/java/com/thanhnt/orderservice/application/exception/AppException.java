package com.thanhnt.orderservice.application.exception;

import com.thanhnt.orderservice.domain.entity.common.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AppException extends RuntimeException {
  private final String errorCode;
  private final String message;

  public AppException(ErrorCode errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode.getCode();
    this.message = errorCode.getMessage();
  }
}
