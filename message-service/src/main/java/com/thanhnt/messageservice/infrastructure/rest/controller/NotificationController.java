package com.thanhnt.messageservice.infrastructure.rest.controller;

import com.thanhnt.messageservice.api.facade.NotificationFacade;
import com.thanhnt.messageservice.api.response.BaseResponse;
import com.thanhnt.messageservice.api.response.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationFacade notificationFacade;

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      tags = "Notification APIs",
      summary = "Get Notifications of user and mark them as read")
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("isAuthenticated()")
  public BaseResponse<PaginationResponse> getNotification(
      @RequestParam int page, @RequestParam int size) {
    return this.notificationFacade.getNotification(page, size);
  }
}
