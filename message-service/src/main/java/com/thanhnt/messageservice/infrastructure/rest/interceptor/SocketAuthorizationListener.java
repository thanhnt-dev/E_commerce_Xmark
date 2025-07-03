package com.thanhnt.messageservice.infrastructure.rest.interceptor;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.HandshakeData;
import com.thanhnt.messageservice.application.dto.ValidateTokenDTO;
import com.thanhnt.messageservice.application.service.UserServiceFeign;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocketAuthorizationListener implements AuthorizationListener {

  private final UserServiceFeign userService;

  @Override
  public AuthorizationResult getAuthorizationResult(HandshakeData handshakeData) {
    try {
      String token = handshakeData.getUrlParams().get("token").get(0);
      ValidateTokenDTO validateTokenResponse = userService.validateToken(token);

      if (null != validateTokenResponse) {
        List<GrantedAuthority> authorityList =
            validateTokenResponse.getRoles().stream()
                .map(rolesEnum -> new SimpleGrantedAuthority(rolesEnum.getRoleName()))
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                validateTokenResponse.getId(), null, authorityList);
        handshakeData.setAuthToken(authentication);
      }
      return AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      return AuthorizationResult.FAILED_AUTHORIZATION;
    }
  }
}
