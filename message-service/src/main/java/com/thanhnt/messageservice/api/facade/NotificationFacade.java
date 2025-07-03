package com.thanhnt.messageservice.api.facade;

import com.thanhnt.messageservice.api.response.BaseResponse;
import com.thanhnt.messageservice.api.response.PaginationResponse;
import com.thanhnt.messageservice.application.dto.NotificationDTO;

public interface NotificationFacade {
  void consumeNotification(NotificationDTO notificationDTO);

  BaseResponse<PaginationResponse> getNotification(int page, int size);
}
