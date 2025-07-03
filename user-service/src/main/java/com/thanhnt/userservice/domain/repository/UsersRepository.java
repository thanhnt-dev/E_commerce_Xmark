package com.thanhnt.userservice.domain.repository;

import com.thanhnt.userservice.domain.entity.users.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository
    extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {
  Optional<Users> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);
}
