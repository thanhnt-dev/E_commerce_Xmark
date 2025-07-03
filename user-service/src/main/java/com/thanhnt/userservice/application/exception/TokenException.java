package com.thanhnt.userservice.application.exception;

import com.thanhnt.userservice.domain.entity.commons.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenException extends RuntimeException {
  private final String errorCode;
  private final String message;

  public TokenException(ErrorCode errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode.getCode();
    this.message = errorCode.getMessage();
  }
}
