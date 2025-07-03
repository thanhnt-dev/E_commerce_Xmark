package com.thanhnt.userservice.api.facade;

import com.thanhnt.userservice.api.request.RoleUpdateRequest;
import com.thanhnt.userservice.api.request.UpdateProfileRequest;
import com.thanhnt.userservice.api.request.UpsertAddressRequest;
import com.thanhnt.userservice.api.response.AddressResponse;
import com.thanhnt.userservice.api.response.BaseResponse;
import com.thanhnt.userservice.api.response.UserProfileResponse;
import com.thanhnt.userservice.application.dto.CustomerInfoDTO;
import com.thanhnt.userservice.application.dto.StoreOwnerDetailDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserFacade {
  BaseResponse<UserProfileResponse> getProfile();

  BaseResponse<UserProfileResponse> updateProfile(UpdateProfileRequest request);

  BaseResponse<String> updateAvatar(MultipartFile file);

  BaseResponse<List<AddressResponse>> upsertAddress(UpsertAddressRequest request);

  BaseResponse<Void> deleteAddress(Long addressId);

  ResponseEntity<StoreOwnerDetailDTO> getStoreOwnerDetail(Long id);

  ResponseEntity<Boolean> updateRoleByAdmin(RoleUpdateRequest request);

  ResponseEntity<CustomerInfoDTO> getCustomerInfo(Long id);
}
