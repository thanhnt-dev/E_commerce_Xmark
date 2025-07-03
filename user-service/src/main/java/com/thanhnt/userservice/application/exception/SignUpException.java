package com.thanhnt.userservice.application.exception;

import com.thanhnt.userservice.domain.entity.commons.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpException extends RuntimeException {
  private final String errorCode;
  private final String message;

  public SignUpException(ErrorCode errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode.getCode();
    this.message = errorCode.getMessage();
  }
}
