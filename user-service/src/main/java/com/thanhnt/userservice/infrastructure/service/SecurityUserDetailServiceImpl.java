package com.thanhnt.userservice.infrastructure.service;

import com.thanhnt.userservice.api.request.SignUpRequest;
import com.thanhnt.userservice.application.exception.SignUpException;
import com.thanhnt.userservice.application.exception.UserException;
import com.thanhnt.userservice.application.service.AccountService;
import com.thanhnt.userservice.domain.entity.commons.ErrorCode;
import com.thanhnt.userservice.domain.entity.users.Users;
import com.thanhnt.userservice.domain.repository.UsersRepository;
import com.thanhnt.userservice.infrastructure.security.SecurityUserDetail;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailServiceImpl implements AccountService {

  private final UsersRepository usersRepository;

  @Override
  public Users findByEmail(String email) {
    return usersRepository
        .findByEmail(email)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  public UserDetails getUserDetailByEmail(String email) {
    var user = usersRepository.findByEmail(email).orElse(null);
    if (user == null) return null;

    List<GrantedAuthority> authorityList =
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getRoleName().getRoleName()))
            .collect(Collectors.toList());
    return SecurityUserDetail.build(user, authorityList);
  }

  @Override
  public boolean existByPhone(String phone) {
    return usersRepository.existsByPhone(phone);
  }

  @Override
  public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
    var user =
        usersRepository
            .findByEmail(mail)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

    List<GrantedAuthority> authorityList =
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getRoleName().getRoleName()))
            .collect(Collectors.toList());
    return SecurityUserDetail.build(user, authorityList);
  }

  @Override
  public void validateSignUp(SignUpRequest request) {
    boolean isExistsByPhoneNumber = usersRepository.existsByPhone(request.getPhone());
    boolean isExistsByEmail = usersRepository.existsByEmail(request.getEmail());

    boolean isPhoneAndEmailExisted = isExistsByPhoneNumber && isExistsByEmail;

    if (isPhoneAndEmailExisted) throw new SignUpException(ErrorCode.PHONE_AND_MAIL_EXIST);
    if (isExistsByEmail) throw new SignUpException(ErrorCode.EMAIL_EXIST);
    if (isExistsByPhoneNumber) throw new SignUpException(ErrorCode.PHONE_EXIST);
  }

  @Override
  @Transactional
  public void updateUser(Users users) {
    usersRepository.save(users);
  }

  @Override
  @Transactional
  public Users signUp(Users users) {
    return usersRepository.save(users);
  }
}
