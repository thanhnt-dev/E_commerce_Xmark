package com.thanhnt.messageservice.application.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.thanhnt.messageservice.application.dto.MessageDTO;

public interface ChatSocketService {

  void saveMessage(SocketIOClient senderClient, MessageDTO message);
}
