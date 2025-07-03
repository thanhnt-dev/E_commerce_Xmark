package com.thanhnt.messageservice.domain.repository;

import com.thanhnt.messageservice.domain.entity.notifications.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
  Page<Notification> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
