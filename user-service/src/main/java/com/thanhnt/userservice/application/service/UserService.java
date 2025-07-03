package com.thanhnt.userservice.application.service;

import com.thanhnt.userservice.api.request.UserCriteria;
import com.thanhnt.userservice.domain.entity.users.Users;
import org.springframework.data.domain.Page;

public interface UserService {
  Users getOwnerStoreById(Long id);

  Users findById(Long id);

  void saveUser(Users user);

  Page<Users> findAllByCriteria(UserCriteria userCriteria);
}
