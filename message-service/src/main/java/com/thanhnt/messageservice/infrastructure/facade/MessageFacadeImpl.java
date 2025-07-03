package com.thanhnt.messageservice.infrastructure.facade;

import com.thanhnt.messageservice.api.facade.MessageFacade;
import com.thanhnt.messageservice.api.request.GetMessageCriteria;
import com.thanhnt.messageservice.api.response.*;
import com.thanhnt.messageservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.messageservice.application.dto.UserSnapshotDTO;
import com.thanhnt.messageservice.application.dto.ValidateTokenDTO;
import com.thanhnt.messageservice.application.service.CacheService;
import com.thanhnt.messageservice.application.service.MessageService;
import com.thanhnt.messageservice.domain.entity.message.Message;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageFacadeImpl implements MessageFacade {

  private final MessageService messageService;
  private final CacheService cacheService;

  @Value("${thanhnt.userCacheKey}")
  private String userKeyCachePrefix;

  @Value("${thanhnt.shopCacheKey}")
  private String shopKeyCachePrefix;

  @Override
  public BaseResponse<List<ChatListMessageResponse>> getAllChatListMessage() {
    var user =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<Message> latestMessages = messageService.getChatListMessages(user.getId());

    List<ChatListMessageResponse> chatListMessages =
        latestMessages.stream()
            .map(
                message -> {
                  Long receiverId =
                      message.getSenderId().equals(user.getId())
                          ? message.getReceiverId()
                          : message.getSenderId();
                  String receiverKey = userKeyCachePrefix + receiverId;

                  UserSnapshotDTO receiver = cacheService.retrieveUserSnapshot(receiverKey);

                  return ChatListMessageResponse.builder()
                      .receiverId(receiverId)
                      .receiverName(receiver.getFullName())
                      .receiverAvatar(receiver.getAvatar())
                      .roomId(message.getRoomId())
                      .latestMessage(message.getContent())
                      .latestMessageAt(
                          LocalDateTime.ofInstant(
                              Instant.ofEpochMilli(message.getCreatedAt()), ZoneId.systemDefault()))
                      .build();
                })
            .collect(Collectors.toList());

    return BaseResponse.build(chatListMessages, true);
  }

  @Override
  public BaseResponse<PaginationResponse> getMessages(
      Long recipientId, GetMessageCriteria criteria) {
    var user =
        (ValidateTokenDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    var pageableMessage = messageService.getMessages(user.getId(), recipientId, criteria);

    var messages = pageableMessage.getContent();

    if (messages.isEmpty()) {
      return BaseResponse.build(
          PaginationResponse.build(
              Collections.emptyList(), pageableMessage, criteria.getCurrentPage(), true),
          true);
    }
    var messageFirst = messages.get(0);

    SnapshotWrapper sender =
        getSnapshot(messageFirst.getSenderType().isUser(), messageFirst.getSenderId());
    SnapshotWrapper receiver =
        getSnapshot(messageFirst.getReceiverType().isUser(), messageFirst.getReceiverId());

    var messageResponses =
        messages.stream()
            .map(
                message ->
                    MessageContentResponse.builder()
                        .messageId(message.getId())
                        .content(
                            message.getContent() != null
                                ? message.getContent()
                                : message.getSecretUrl())
                        .isImage(message.getContent() == null)
                        .timeAt(
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(message.getCreatedAt()),
                                ZoneId.systemDefault()))
                        .build())
            .collect(Collectors.toList());
    Collections.reverse(messageResponses);

    var chatResponse =
        MessageDetailResponse.builder()
            .senderId(messageFirst.getSenderId())
            .senderType(messageFirst.getSenderType())
            .senderFullName(sender.name() != null ? sender.name() : "")
            .senderAvatar(sender.avatar() != null ? sender.avatar() : "")
            .recipientId(messageFirst.getReceiverId())
            .recipientType(messageFirst.getReceiverType())
            .recipientFullName(receiver.name() != null ? receiver.name() : "")
            .recipientAvatar(receiver.avatar() != null ? receiver.avatar() : "")
            .messageContents(messageResponses)
            .build();
    return BaseResponse.build(
        PaginationResponse.build(chatResponse, pageableMessage, criteria.getCurrentPage(), true),
        true);
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
