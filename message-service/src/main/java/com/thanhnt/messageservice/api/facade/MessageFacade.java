package com.thanhnt.messageservice.api.facade;

import com.thanhnt.messageservice.api.request.GetMessageCriteria;
import com.thanhnt.messageservice.api.response.BaseResponse;
import com.thanhnt.messageservice.api.response.ChatListMessageResponse;
import com.thanhnt.messageservice.api.response.PaginationResponse;
import java.util.List;

public interface MessageFacade {

  BaseResponse<List<ChatListMessageResponse>> getAllChatListMessage();

  BaseResponse<PaginationResponse> getMessages(Long recipientId, GetMessageCriteria criteria);
}
