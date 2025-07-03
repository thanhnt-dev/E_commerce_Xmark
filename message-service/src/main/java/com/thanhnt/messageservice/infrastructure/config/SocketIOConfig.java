package com.thanhnt.messageservice.infrastructure.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.thanhnt.messageservice.infrastructure.rest.interceptor.SocketAuthorizationListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SocketIOConfig {

  @Value("${socketio.hostname}")
  private String host;

  @Value("${socketio.port}")
  private Integer port;

  @Value("${socketio.origin}")
  private String origin;

  private final SocketAuthorizationListener socketAuthorizationListener;

  @Bean
  public SocketIOServer socketIOServer() {
    com.corundumstudio.socketio.Configuration config =
        new com.corundumstudio.socketio.Configuration();
    config.setHostname(host);
    config.setPort(port);
    config.setAuthorizationListener(socketAuthorizationListener);
    config.setOrigin(origin);
    config.setTransports(Transport.WEBSOCKET, Transport.POLLING);
    return new SocketIOServer(config);
  }
}
