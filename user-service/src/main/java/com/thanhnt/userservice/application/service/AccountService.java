package com.thanhnt.userservice.application.service;

import com.thanhnt.userservice.api.request.SignUpRequest;
import com.thanhnt.userservice.domain.entity.users.Users;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {
  Users findByEmail(String mail);

  UserDetails getUserDetailByEmail(String email);

  boolean existByPhone(String phone);

  void validateSignUp(SignUpRequest request);

  void updateUser(Users users);

  Users signUp(Users users);
}
