package com.thanhnt.userservice.infrastructure.rest.interceptor;

import com.thanhnt.userservice.api.response.BaseResponse;
import com.thanhnt.userservice.api.response.ExceptionResponse;
import com.thanhnt.userservice.application.exception.*;
import com.thanhnt.userservice.domain.entity.commons.ErrorCode;
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

  @ExceptionHandler(value = {UserException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleUserException(
      UserException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(exception.getErrorCode(), exception.getMessage()), false),
        HttpStatus.BAD_REQUEST);
  }

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

  @ExceptionHandler(value = {SignUpException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleSignupException(
      SignUpException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(exception.getErrorCode(), exception.getMessage()), false),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {OTPException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleOTPException(
      OTPException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(exception.getErrorCode(), exception.getMessage()), false),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {LoginException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleLoginException(
      LoginException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(exception.getErrorCode(), exception.getMessage()), false),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {ChangePasswordException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleChangePasswordException(
      ChangePasswordException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(exception.getErrorCode(), exception.getMessage()), false),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {UpdateUserException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleUpdateUserException(
      UpdateUserException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(exception.getErrorCode(), exception.getMessage()), false),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleBadCredentialsException(
      BadCredentialsException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(
                ErrorCode.BAD_CREDENTIAL_LOGIN.getCode(),
                ErrorCode.BAD_CREDENTIAL_LOGIN.getMessage()),
            false),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AddressException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<BaseResponse<ExceptionResponse>> handleAddressException(
      AddressException exception) {
    return new ResponseEntity<>(
        BaseResponse.build(
            new ExceptionResponse(
                ErrorCode.BAD_CREDENTIAL_LOGIN.getCode(),
                ErrorCode.BAD_CREDENTIAL_LOGIN.getMessage()),
            false),
        HttpStatus.BAD_REQUEST);
  }
}
