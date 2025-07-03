package com.thanhnt.userservice.domain.repository;

import com.thanhnt.userservice.domain.entity.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, Long> {}
