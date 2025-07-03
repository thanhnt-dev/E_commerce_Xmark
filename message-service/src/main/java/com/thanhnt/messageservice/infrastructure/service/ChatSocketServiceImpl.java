package com.thanhnt.messageservice.infrastructure.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.thanhnt.messageservice.api.response.ChatListMessageResponse;
import com.thanhnt.messageservice.api.response.MessageResponse;
import com.thanhnt.messageservice.application.dto.MessageDTO;
import com.thanhnt.messageservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.messageservice.application.dto.SocketDTO;
import com.thanhnt.messageservice.application.dto.UserSnapshotDTO;
import com.thanhnt.messageservice.application.service.CacheService;
import com.thanhnt.messageservice.application.service.ChatSocketService;
import com.thanhnt.messageservice.application.service.MessageService;
import com.thanhnt.messageservice.domain.entity.common.SocketType;
import com.thanhnt.messageservice.domain.entity.message.Message;
import com.thanhnt.messageservice.infrastructure.socket.SocketSessionRegistry;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatSocketServiceImpl implements ChatSocketService {

  private final MessageService messageService;
  private final SocketIOServer socketIOServer;
  private final CacheService cacheService;
  private final SocketSessionRegistry socketSessionRegistry;

  @Value("${thanhnt.userCacheKey}")
  private String userKeyCachePrefix;

  @Value("${thanhnt.shopCacheKey}")
  private String shopKeyCachePrefix;

  private static final Logger log = LoggerFactory.getLogger(ChatSocketServiceImpl.class);

  @Override
  public void saveMessage(SocketIOClient senderClient, MessageDTO messageDTO) {
    String roomId = generateRoomId(messageDTO.getSenderId(), messageDTO.getReceiverId());
    log.info("Saving message from Socket.IO: {}", messageDTO);
    messageService.sendMessageToKafka(messageDTO);
  }

  private String generateRoomId(Long senderId, Long receiverId) {
    return Math.min(senderId, receiverId) + "-" + Math.max(senderId, receiverId);
  }

  @KafkaListener(topics = "${spring.kafka.topics.private}", groupId = "chat-group")
  @Transactional
  public void handleChatMessage(MessageDTO messageDTO) {
    log.info("Consumed chat message from Kafka: {}", messageDTO);
    String roomId = generateRoomId(messageDTO.getSenderId(), messageDTO.getReceiverId());
    Message message = messageService.saveMessage(messageDTO, roomId);

    log.info(
        "Sending socket message to room {}: messageId={}, senderId={}, senderType={}, receiverId={}, receiverType={}, content={}",
        roomId,
        message.getId(),
        message.getSenderId(),
        message.getSenderType(),
        message.getReceiverId(),
        message.getReceiverType(),
        message.getContent());

    SnapshotWrapper sender = getSnapshot(message.getSenderType().isUser(), message.getSenderId());

    SnapshotWrapper receiver =
        getSnapshot(message.getReceiverType().isUser(), message.getReceiverId());

    MessageResponse response =
        MessageResponse.builder()
            .messageId(message.getId())
            .senderType(message.getSenderType())
            .senderId(sender.id())
            .senderFullName(sender.name())
            .senderAvatar(sender.avatar())
            .recipientType(message.getReceiverType())
            .recipientId(receiver.id())
            .recipientFullName(receiver.name())
            .recipientAvatar(receiver.avatar())
            .content(message.getContent())
            .build();

    socketIOServer
        .getRoomOperations(roomId)
        .sendEvent("receive_message", SocketDTO.of(SocketType.CHAT, response));

    ChatListMessageResponse senderListMsg =
        ChatListMessageResponse.builder()
            .receiverId(receiver.id())
            .receiverName(receiver.name())
            .receiverAvatar(receiver.avatar())
            .latestMessage(message.getContent())
            .latestMessageAt(
                Instant.ofEpochMilli(message.getCreatedAt())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime())
            .build();

    ChatListMessageResponse receiverListMsg =
        ChatListMessageResponse.builder()
            .receiverId(sender.id())
            .receiverName(sender.name())
            .receiverAvatar(sender.avatar())
            .latestMessage(message.getContent())
            .latestMessageAt(
                Instant.ofEpochMilli(message.getCreatedAt())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime())
            .build();

    sendChatListUpdate(sender.id(), senderListMsg);
    sendChatListUpdate(receiver.id(), receiverListMsg);
  }

  private void sendChatListUpdate(Long userId, ChatListMessageResponse response) {
    UUID socketId = socketSessionRegistry.getSessionId(userId);
    if (socketId != null) {
      SocketDTO dto = SocketDTO.of(SocketType.CHAT_LIST, response);
      socketIOServer.getClient(socketId).sendEvent("chat_list", dto);
    }
  }

  private SnapshotWrapper getSnapshot(boolean isUser, Long id) {
    String key = (isUser ? userKeyCachePrefix : shopKeyCachePrefix) + id;
    Object snapshot =
        isUser ? cacheService.retrieveUserSnapshot(key) : cacheService.retrieveShopSnapshot(key);

    if (snapshot == null) {
      throw new RuntimeException(
          "Cannot find " + (isUser ? "USER" : "SHOP") + " with ID " + id + " in cache");
    }

    if (isUser) {
      UserSnapshotDTO dto = (UserSnapshotDTO) snapshot;
      return new SnapshotWrapper(id, dto.getFullName(), dto.getAvatar());
    } else {
      ShopSnapshotDTO dto = (ShopSnapshotDTO) snapshot;
      return new SnapshotWrapper(id, dto.getShopName(), dto.getAvatar());
    }
  }

  private record SnapshotWrapper(Long id, String name, String avatar) {}
}
