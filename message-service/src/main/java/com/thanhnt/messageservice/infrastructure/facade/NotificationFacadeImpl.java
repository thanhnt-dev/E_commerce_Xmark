package com.thanhnt.messageservice.infrastructure.facade;

import com.corundumstudio.socketio.SocketIOServer;
import com.thanhnt.messageservice.api.facade.NotificationFacade;
import com.thanhnt.messageservice.api.response.BaseResponse;
import com.thanhnt.messageservice.api.response.NotificationResponse;
import com.thanhnt.messageservice.api.response.PaginationResponse;
import com.thanhnt.messageservice.application.dto.NotificationDTO;
import com.thanhnt.messageservice.application.dto.ValidateTokenDTO;
import com.thanhnt.messageservice.application.service.NotificationService;
import com.thanhnt.messageservice.domain.entity.notifications.Notification;
import com.thanhnt.messageservice.infrastructure.socket.SocketSessionRegistry;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationFacadeImpl implements NotificationFacade {

  private final SocketSessionRegistry socketSessionRegistry;
  private final NotificationService notificationService;
  private final SocketIOServer socketIOServer;
  private final Map<Long, Integer> failureCountMap = new ConcurrentHashMap<>();

  private static final Logger LOGGER =
      org.slf4j.LoggerFactory.getLogger(NotificationFacadeImpl.class);

  @KafkaListener(
      topics = "${spring.kafka.topics.public}",
      groupId = "${spring.kafka.consumer.notification-group-id}",
      containerFactory = "notificationKafkaListenerContainerFactory")
  @Override
  public void consumeNotification(NotificationDTO notificationDTO) {
    Long receiverId = notificationDTO.getReceiverId();

    boolean isUserConnected = socketSessionRegistry.isUserConnected(receiverId);
    boolean shouldSend = failureCountMap.getOrDefault(receiverId, 0) < 3;

    if (isUserConnected && shouldSend) {
      try {
        UUID sessionId = socketSessionRegistry.getSessionId(receiverId);
        socketIOServer
            .getClient(sessionId)
            .sendEvent("receive_notification", convertDTOToResponse(notificationDTO));
        LOGGER.info("Sent notification to user {} via session {}", receiverId, sessionId);
        failureCountMap.remove(receiverId); // reset nếu gửi thành công
      } catch (Exception e) {
        int failureCount = failureCountMap.getOrDefault(receiverId, 0) + 1;
        failureCountMap.put(receiverId, failureCount);
        LOGGER.warn(
            "Failed to send notification to user {}. Retry count: {}", receiverId, failureCount);
      }
    } else {
      if (!shouldSend) {
        LOGGER.warn(
            "User {} reached max retry. Notification will not be sent via socket.", receiverId);
      } else {
        LOGGER.info("User {} is offline. Notification not sent via socket.", receiverId);
      }
    }
    notificationService.saveNotification(
        Notification.builder()
            .senderId(notificationDTO.getSenderId())
            .senderName(notificationDTO.getSenderName())
            .content(notificationDTO.getContent())
            .notificationType(notificationDTO.getType())
            .userId(notificationDTO.getReceiverId())
            .build());
  }

  @Override
  public BaseResponse<PaginationResponse> getNotification(int page, int size) {
    var user =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var notifications = notificationService.getNotifications(page, size, user.getId());
    LOGGER.info(
        "User {} retrieved notifications: page {}, size {}, total elements {}",
        user.getId(),
        page,
        size,
        notifications.getTotalElements());

    var response = notifications.map(this::convertEntityToResponse);
    return BaseResponse.build(PaginationResponse.build(response, notifications, page, true), true);
  }

  private NotificationResponse convertEntityToResponse(Notification notification) {
    return NotificationResponse.builder()
        .id(notification.getId())
        .senderId(notification.getSenderId())
        .senderName(notification.getSenderName())
        .content(notification.getContent())
        .isRead(notification.isRead())
        .type(notification.getNotificationType())
        .createdAt(
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(notification.getCreatedAt()), ZoneId.systemDefault()))
        .build();
  }

  private NotificationResponse convertDTOToResponse(NotificationDTO notification) {
    return NotificationResponse.builder()
        .senderId(notification.getSenderId())
        .senderName(notification.getSenderName())
        .content(notification.getContent())
        .isRead(notification.isRead())
        .type(notification.getType())
        .createdAt(
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(notification.getCreatedAt()), ZoneId.systemDefault()))
        .build();
  }
}
