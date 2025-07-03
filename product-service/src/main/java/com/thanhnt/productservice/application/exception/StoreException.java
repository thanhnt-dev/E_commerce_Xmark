package com.thanhnt.productservice.application.exception;

import com.thanhnt.productservice.domain.entity.common.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StoreException extends RuntimeException {
  private final String errorCode;
  private final String message;

  public StoreException(ErrorCode errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode.getCode();
    this.message = errorCode.getMessage();
  }
}
