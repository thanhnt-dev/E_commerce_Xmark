package com.thanhnt.messageservice.infrastructure.rest.controller;

import com.thanhnt.messageservice.api.facade.MessageFacade;
import com.thanhnt.messageservice.api.request.GetMessageCriteria;
import com.thanhnt.messageservice.api.response.BaseResponse;
import com.thanhnt.messageservice.api.response.ChatListMessageResponse;
import com.thanhnt.messageservice.api.response.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
  private final MessageFacade messageFacade;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = "Message APIs", summary = "Get all chat list messages of user")
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("isAuthenticated()")
  public BaseResponse<List<ChatListMessageResponse>> getMessages() {
    return this.messageFacade.getAllChatListMessage();
  }

  @GetMapping("/{recipientId}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(tags = "Message APIs", summary = "Get all messages between two users")
  @SecurityRequirement(name = "Bearer Authentication")
  @PreAuthorize("isAuthenticated()")
  public BaseResponse<PaginationResponse> getMessages(
      @PathVariable Long recipientId, @Nullable GetMessageCriteria criteria) {
    return this.messageFacade.getMessages(recipientId, criteria);
  }
}
