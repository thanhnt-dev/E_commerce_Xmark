package com.thanhnt.messageservice.infrastructure.service;

import com.thanhnt.messageservice.api.request.GetMessageCriteria;
import com.thanhnt.messageservice.application.dto.MessageDTO;
import com.thanhnt.messageservice.application.service.MessageService;
import com.thanhnt.messageservice.domain.entity.message.Message;
import com.thanhnt.messageservice.domain.repository.MessageRepository;
import com.thanhnt.messageservice.domain.specification.MessageSpecification;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
  private final MessageRepository messageRepository;
  private final KafkaTemplate<String, MessageDTO> kafkaTemplate;
  private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

  @Value("${spring.kafka.topics.private}")
  private String privateChatTopic;

  @Override
  @Transactional
  public void sendMessageToKafka(MessageDTO messageDTO) {
    String roomId = messageDTO.getSenderId() + "-" + messageDTO.getReceiverId();
    log.info(
        "Sending message to Kafka: topic={}, roomId={}, message={}",
        privateChatTopic,
        roomId,
        messageDTO);
    kafkaTemplate.send(privateChatTopic, roomId, messageDTO);
  }

  @Override
  @Transactional
  public Message saveMessage(MessageDTO messageDTO, String roomId) {
    Message message =
        Message.builder()
            .senderId(messageDTO.getSenderId())
            .senderType(messageDTO.getSenderType())
            .receiverId(messageDTO.getReceiverId())
            .receiverType(messageDTO.getReceiverType())
            .content(messageDTO.getContent())
            .roomId(roomId)
            .build();
    return messageRepository.save(message);
  }

  @Override
  public List<String> getUserRooms(Long userId) {
    return messageRepository.findBySenderIdOrReceiverId(userId, userId).stream()
        .map(Message::getRoomId)
        .distinct()
        .toList();
  }

  @Override
  public List<Message> getChatListMessages(Long userId) {
    return messageRepository.findLastMessagesByUserId(userId);
  }

  @Override
  public Page<Message> getMessages(Long senderId, Long receiverId, GetMessageCriteria criteria) {
    Pageable pageable =
        PageRequest.of(Math.max(criteria.getCurrentPage() - 1, 0), criteria.getPageSize());

    var specification = MessageSpecification.baseSpecification(senderId, receiverId);

    return messageRepository.findAll(specification, pageable);
  }
}
