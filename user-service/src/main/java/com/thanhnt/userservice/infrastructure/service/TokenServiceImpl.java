package com.thanhnt.userservice.infrastructure.service;

import com.thanhnt.userservice.application.service.TokenService;
import com.thanhnt.userservice.domain.entity.token.RefreshToken;
import com.thanhnt.userservice.domain.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  private final TokenRepository tokenRepository;

  @Override
  @Transactional
  public void saveRefreshToken(RefreshToken token) {
    tokenRepository.save(token);
  }

  @Override
  public void deleteRefreshToken(Long userId) {
    tokenRepository.deleteById(userId);
  }
}
