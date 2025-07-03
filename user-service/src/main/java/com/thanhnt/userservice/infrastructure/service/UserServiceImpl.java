package com.thanhnt.userservice.infrastructure.service;

import com.thanhnt.userservice.api.request.UserCriteria;
import com.thanhnt.userservice.application.exception.UserException;
import com.thanhnt.userservice.application.service.UserService;
import com.thanhnt.userservice.domain.entity.commons.ErrorCode;
import com.thanhnt.userservice.domain.entity.users.Users;
import com.thanhnt.userservice.domain.repository.UsersRepository;
import com.thanhnt.userservice.domain.specifications.UserSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UsersRepository usersRepository;

  @Override
  public Users getOwnerStoreById(Long id) {
    return usersRepository
        .findById(id)
        .orElseThrow(() -> new UserException(ErrorCode.OWNER_NOT_FOUND));
  }

  @Override
  public Users findById(Long id) {
    return usersRepository
        .findById(id)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  @Transactional
  public void saveUser(Users user) {
    usersRepository.save(user);
  }

  @Override
  public Page<Users> findAllByCriteria(UserCriteria userCriteria) {
    Pageable pageable =
        PageRequest.of(Math.max(userCriteria.getCurrentPage() - 1, 0), userCriteria.getPageSize());
    Specification<Users> specification = UserSpecifications.baseSpecification();

    if (userCriteria.getMail() != null) {
      specification = specification.and(UserSpecifications.search(userCriteria.getMail()));
    }
    if (userCriteria.getPhoneNumber() != null) {
      specification =
          specification.and(UserSpecifications.filterByPhone(userCriteria.getPhoneNumber()));
    }
    return usersRepository.findAll(specification, pageable);
  }
}
