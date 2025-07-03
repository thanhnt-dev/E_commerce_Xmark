package com.thanhnt.messageservice.application.service;

import com.thanhnt.messageservice.api.request.GetMessageCriteria;
import com.thanhnt.messageservice.application.dto.MessageDTO;
import com.thanhnt.messageservice.domain.entity.message.Message;
import java.util.List;
import org.springframework.data.domain.Page;

public interface MessageService {
  void sendMessageToKafka(MessageDTO messageDTO);

  Message saveMessage(MessageDTO messageDTO, String roomId);

  List<String> getUserRooms(Long userId);

  List<Message> getChatListMessages(Long userId);

  Page<Message> getMessages(Long senderId, Long receiverId, GetMessageCriteria criterias);
}
