package com.thanhnt.userservice.application.exception;

import com.thanhnt.userservice.domain.entity.commons.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginException extends RuntimeException {
  private final String errorCode;
  private final String message;

  public LoginException(ErrorCode errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode.getCode();
    this.message = errorCode.getMessage();
  }
}
