package com.thanhnt.storeservice.api.facade;

import com.thanhnt.storeservice.api.request.StoreCriteria;
import com.thanhnt.storeservice.api.request.UpdateVerifyStatusStoreRequest;
import com.thanhnt.storeservice.api.request.UpsertShopRequest;
import com.thanhnt.storeservice.api.response.*;
import com.thanhnt.storeservice.application.dto.ShopSnapshotDTO;
import com.thanhnt.storeservice.application.dto.UpdateProductShopDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ShopFacade {
  BaseResponse<Long> upsertShop(UpsertShopRequest request);

  BaseResponse<Void> uploadImageIdentity(MultipartFile frontIdentity, MultipartFile backIdentity)
      throws IOException;

  BaseResponse<PaginationResponse<List<StoreResponse>>> getStoresByAdminWithFilter(
      StoreCriteria criteria);

  BaseResponse<StoreDetailsResponse> getStoreDetailsByAdmin(Long id);

  BaseResponse<Void> updateStoreApprovalStatus(Long id, UpdateVerifyStatusStoreRequest request);

  BaseResponse<StoreDetailsResponse> getStoreDetailsByOwner();

  BaseResponse<String> uploadAvatar(MultipartFile avatar) throws IOException;

  BaseResponse<ShopInfoResponse> getShopInfoById(Long shopId);

  ResponseEntity<ShopSnapshotDTO> checkValidShopById(Long id);

  ResponseEntity<ShopSnapshotDTO> getInfoShopByShopId(Long shopId);

  ResponseEntity<Void> addProductId(UpdateProductShopDTO updateProductShopDTO);
}
