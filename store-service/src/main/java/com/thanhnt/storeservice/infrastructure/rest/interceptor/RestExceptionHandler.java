package com.thanhnt.storeservice.infrastructure.rest.interceptor;

import com.thanhnt.storeservice.api.response.BaseResponse;
import com.thanhnt.storeservice.api.response.ExceptionResponse;
import com.thanhnt.storeservice.application.exception.ShopException;
import com.thanhnt.storeservice.application.exception.UnauthorizedException;
import com.thanhnt.storeservice.domain.entity.common.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public ResponseEntity<BaseResponse<List<ExceptionResponse>>>
      handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<ExceptionResponse> responses =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new ExceptionResponse(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

    BaseResponse<List<ExceptionResponse>> response = BaseResponse.build(responses, false);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleBadCredentialsException(
      BadCredentialsException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(
                ErrorCode.INVALIDATE.getCode(), ErrorCode.INVALIDATE.getMessage()),
            false),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {ShopException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleShopException(
      ShopException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(exception.getErrorCode(), exception.getMessage()), false),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {UnauthorizedException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleUnauthorizedException(
      UnauthorizedException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(exception.getErrorCode(), exception.getMessage()), false),
        HttpStatus.BAD_REQUEST);
  }
}
