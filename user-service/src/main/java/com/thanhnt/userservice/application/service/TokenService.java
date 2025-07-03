package com.thanhnt.userservice.application.service;

import com.thanhnt.userservice.domain.entity.token.RefreshToken;

public interface TokenService {
  void saveRefreshToken(RefreshToken refreshToken);

  void deleteRefreshToken(Long userId);
}
