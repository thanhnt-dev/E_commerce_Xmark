package com.thanhnt.userservice.infrastructure.service;

import com.thanhnt.userservice.api.response.TokenResponse;
import com.thanhnt.userservice.application.service.JwtTokenService;
import com.thanhnt.userservice.infrastructure.security.SecurityUserDetail;
import io.jsonwebtoken.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
  @Value("${spring.jwt.secretKey}")
  private String secretKey;

  @Value("${spring.jwt.accessTokenExpirationTime}")
  private String accessTokenExpirationTime;

  @Value("${spring.jwt.refreshTokenExpirationTime}")
  private String refreshTokenExpirationTime;

  @Override
  public TokenResponse generateToken(SecurityUserDetail user) {
    return TokenResponse.builder()
        .accessToken(generateAccessToken(user))
        .refreshToken(generateRefreshToken(user))
        .build();
  }

  private String generateAccessToken(SecurityUserDetail user) {
    Map<String, Object> claims = getClaims(user);
    return Jwts.builder()
        .setSubject(user.getEmail())
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(
            new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime)))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  private String generateRefreshToken(SecurityUserDetail user) {
    Map<String, Object> claims = getClaims(user);
    return Jwts.builder()
        .setSubject(user.getEmail())
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(
            new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime)))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  @Override
  public Boolean validateToken(String token) {
    if (null == token) return false;
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parse(token);
    } catch (SignatureException
        | MalformedJwtException
        | ExpiredJwtException
        | UnsupportedJwtException
        | IllegalArgumentException exception) {
    }
    return true;
  }

  @Override
  public String getEmailFromJwtToken(String token) {
    return Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody()
        .get("mail", String.class);
  }

  @Override
  public Long getExpirationDateFromJwtToken(String token) {
    return Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody()
        .get("exp", Long.class);
  }

  private Map<String, Object> getClaims(SecurityUserDetail userDetail) {
    List<String> roles =
        userDetail.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userDetail.getId());
    claims.put("mail", userDetail.getEmail());
    claims.put("phone", userDetail.getPhone());
    claims.put("username", userDetail.getUsername());
    claims.put("roles", roles);
    return claims;
  }
}
