package com.thanhnt.userservice.infrastructure.facade;

import com.thanhnt.userservice.api.facade.DashboardFacade;
import com.thanhnt.userservice.api.request.UserCriteria;
import com.thanhnt.userservice.api.response.BaseResponse;
import com.thanhnt.userservice.api.response.PaginationResponse;
import com.thanhnt.userservice.api.response.UserResponse;
import com.thanhnt.userservice.application.service.CloudinaryService;
import com.thanhnt.userservice.application.service.UserService;
import com.thanhnt.userservice.domain.entity.role.RolesEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DashboardFacadeImpl implements DashboardFacade {
  private final UserService userService;
  private final CloudinaryService cloudinaryService;

  @Override
  public BaseResponse<PaginationResponse<List<UserResponse>>> getUserByFilter(
      UserCriteria userCriteria) {
    log.info("Fetching users with criteria: {}", userCriteria);
    var userPage = userService.findAllByCriteria(userCriteria);
    List<UserResponse> userResponses =
        userPage.getContent().stream().map(this::fromEntity).toList();
    return BaseResponse.build(
        PaginationResponse.build(userResponses, userPage, userCriteria.getCurrentPage(), true),
        true);
  }

  private UserResponse fromEntity(com.thanhnt.userservice.domain.entity.users.Users user) {
    RolesEnum role =
        user.getRoles().stream().anyMatch(r -> r.getRoleName().equals(RolesEnum.ROLE_STORE_OWNER))
            ? RolesEnum.ROLE_STORE_OWNER
            : RolesEnum.ROLE_USER;

    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .phone(user.getPhone())
        .fullName(user.getFirstName() + " " + user.getLastName())
        .avatar(user.getAvatar() != null ? cloudinaryService.getImageUrl(user.getAvatar()) : null)
        .createdAt(user.getCreatedAt())
        .status(user.isActive())
        .role(role)
        .build();
  }
}
