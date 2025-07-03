package com.thanhnt.messageservice.infrastructure.socket;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SocketSessionRegistry {
  private final Map<Long, UUID> userSessions = new ConcurrentHashMap<>();

  public void registerSession(Long userId, UUID sessionId) {
    userSessions.put(userId, sessionId);
  }

  public void removeSession(UUID sessionId) {
    userSessions.values().removeIf(value -> value.equals(sessionId));
  }

  public UUID getSessionId(Long userId) {
    return userSessions.get(userId);
  }

  public boolean isUserConnected(Long userId) {
    return userSessions.containsKey(userId);
  }
}
