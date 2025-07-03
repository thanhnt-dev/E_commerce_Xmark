package com.thanhnt.storeservice.application.exception;

import com.thanhnt.storeservice.domain.entity.common.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UnauthorizedException extends RuntimeException {
  private final String errorCode;
  private final String message;

  public UnauthorizedException(ErrorCode errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode.getCode();
    this.message = errorCode.getMessage();
  }
}
