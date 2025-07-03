package com.thanhnt.messageservice.domain.repository;

import com.thanhnt.messageservice.domain.entity.message.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository
    extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
  List<Message> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

  @Query(
      value =
          """
    SELECT DISTINCT ON (
        LEAST(m.sender_id, m.receiver_id),
        GREATEST(m.sender_id, m.receiver_id)
    ) *
    FROM messages m
    WHERE m.sender_id = :userId OR m.receiver_id = :userId
    ORDER BY
        LEAST(m.sender_id, m.receiver_id),
        GREATEST(m.sender_id, m.receiver_id),
        m.created_at DESC
    """,
      nativeQuery = true)
  List<Message> findLastMessagesByUserId(@Param("userId") Long userId);
}
