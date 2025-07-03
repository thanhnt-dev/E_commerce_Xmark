package com.thanhnt.messageservice.domain.entity.message;

import com.thanhnt.messageservice.domain.entity.common.BaseEntity;
import com.thanhnt.messageservice.domain.entity.common.ParticipantType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Message extends BaseEntity implements Serializable {

  @Column(name = "sender_id", nullable = false)
  private Long senderId;

  @Column(name = "sender_type", nullable = false)
  private ParticipantType senderType;

  @Column(name = "receiver_id", nullable = false)
  private Long receiverId;

  @Column(name = "receiver_type", nullable = false)
  private ParticipantType receiverType;

  @Column(name = "room_id", nullable = false)
  private String roomId;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "asset_id")
  private String assetId;

  @Column(name = "secret_url")
  private String secretUrl;
}
