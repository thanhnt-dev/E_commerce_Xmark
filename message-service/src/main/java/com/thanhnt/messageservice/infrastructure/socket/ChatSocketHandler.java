package com.thanhnt.messageservice.infrastructure.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.thanhnt.messageservice.application.dto.MessageDTO;
import com.thanhnt.messageservice.application.service.ChatSocketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatSocketHandler {
  private final ChatSocketService chatSocketService;
  private final SocketIOServer socketIOServer;
  private static final Logger log = LoggerFactory.getLogger(ChatSocketHandler.class);

  public DataListener<MessageDTO> onChatReceived() {
    return (senderClient, data, ackSender) -> {
      log.info(data.toString());
      if (data.getReceiverId() == null
          || data.getContent() == null
          || data.getType() == null
          || data.getSenderId() == null) {
        log.error("Invalid message data: {}", data);
        if (ackSender.isAckRequested()) {
          ackSender.sendAckData("Error: Invalid message data");
        }
        return;
      }
      String roomId = generateRoomId(data.getSenderId(), data.getReceiverId());
      joinUsersToRoom(senderClient, data.getSenderId(), data.getReceiverId(), roomId);
      chatSocketService.saveMessage(senderClient, data);

      if (ackSender.isAckRequested()) {
        ackSender.sendAckData("Message sent successfully");
      }
    };
  }

  private String generateRoomId(Long senderId, Long receiverId) {
    return Math.min(senderId, receiverId) + "-" + Math.max(senderId, receiverId);
  }

  private void joinUsersToRoom(
      SocketIOClient senderClient, Long senderId, Long receiverId, String roomId) {
    senderClient.joinRoom(roomId);
    log.info("Sender {} joined room {}", senderId, roomId);

    socketIOServer
        .getAllClients()
        .forEach(
            client -> {
              UsernamePasswordAuthenticationToken auth =
                  (UsernamePasswordAuthenticationToken) client.getHandshakeData().getAuthToken();
              if (auth != null
                  && auth.getPrincipal() != null
                  && auth.getPrincipal().toString().equals(receiverId.toString())) {
                client.joinRoom(roomId);
                log.info(
                    "Receiver {} joined room {} with session {}",
                    receiverId,
                    roomId,
                    client.getSessionId());
              }
            });
  }
}
