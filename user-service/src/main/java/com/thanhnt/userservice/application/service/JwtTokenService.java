package com.thanhnt.userservice.application.service;

import com.thanhnt.userservice.api.response.TokenResponse;
import com.thanhnt.userservice.infrastructure.security.SecurityUserDetail;

public interface JwtTokenService {
  TokenResponse generateToken(SecurityUserDetail user);

  Boolean validateToken(String token);

  String getEmailFromJwtToken(String token);

  Long getExpirationDateFromJwtToken(String token);
}
