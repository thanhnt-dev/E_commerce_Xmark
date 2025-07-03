package com.thanhnt.messageservice.infrastructure.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.thanhnt.messageservice.application.dto.MessageDTO;
import com.thanhnt.messageservice.application.service.MessageService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SocketModule {
  private final SocketIOServer socketIOServer;
  private final ChatSocketHandler chatSocketHandler;
  private final MessageService messageService;
  private final SocketSessionRegistry socketSessionRegistry;

  @PostConstruct
  public void init() {
    socketIOServer.addConnectListener(onConnected());
    socketIOServer.addDisconnectListener(onDisconnected());
    socketIOServer.addEventListener(
        "send_message", MessageDTO.class, chatSocketHandler.onChatReceived());
  }

  private com.corundumstudio.socketio.listener.ConnectListener onConnected() {
    return client -> {
      UsernamePasswordAuthenticationToken authentication =
          (UsernamePasswordAuthenticationToken) client.getHandshakeData().getAuthToken();
      if (authentication == null || authentication.getPrincipal() == null) {
        log.error("No authenticated user for client: {}", client.getSessionId());
        client.disconnect();
        return;
      }
      socketSessionRegistry.registerSession(
          Long.parseLong(authentication.getPrincipal().toString()), client.getSessionId());

      List<String> rooms =
          messageService.getUserRooms(Long.parseLong(authentication.getPrincipal().toString()));
      for (String room : rooms) {
        client.joinRoom(room);
        log.info("Socket ID[{}] joined room[{}]", client.getSessionId().toString(), room);
      }
      log.info(
          "Socket ID[{}] Connected for userId[{}]",
          client.getSessionId().toString(),
          Long.parseLong(authentication.getPrincipal().toString()));
    };
  }

  private com.corundumstudio.socketio.listener.DisconnectListener onDisconnected() {
    return client -> {
      socketSessionRegistry.removeSession(client.getSessionId());
      UsernamePasswordAuthenticationToken authentication =
          (UsernamePasswordAuthenticationToken) client.getHandshakeData().getAuthToken();
      if (authentication != null && authentication.getPrincipal() != null) {
        log.info(
            "Socket ID[{}] Disconnected for userId[{}]",
            client.getSessionId().toString(),
            authentication.getPrincipal().toString());
      } else {
        log.info("Socket ID[{}] Disconnected (no user)", client.getSessionId().toString());
      }
    };
  }
}
