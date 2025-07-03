package com.thanhnt.userservice.infrastructure.service;

import com.cloudinary.utils.ObjectUtils;
import com.thanhnt.userservice.application.service.CloudinaryService;
import com.thanhnt.userservice.infrastructure.config.CloudinaryConfig;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
  private final CloudinaryConfig cloudinaryConfig;

  @Value("${thanhnt.defaultAvatar}")
  private String defaultImage;

  @Override
  public Map<String, String> uploadImage(byte[] image) {
    var params =
        ObjectUtils.asMap(
            "folder", "eCommerce/avatar",
            "resource_type", "image");
    try {
      var uploadResult = cloudinaryConfig.cloudinary().uploader().upload(image, params);
      String assetId = uploadResult.get("asset_id").toString();
      String publicId = uploadResult.get("public_id").toString();

      Map<String, String> result = new HashMap<>();
      result.put("asset_id", assetId);
      result.put("public_id", publicId);
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getImageUrl(String assetKey) {
    try {
      var imageUrl =
          cloudinaryConfig.cloudinary().api().resourceByAssetID(assetKey, ObjectUtils.emptyMap());
      return imageUrl.get("secure_url").toString();
    } catch (Exception e) {
      return defaultImage;
    }
  }

  @Override
  public boolean deleteImage(String mediaKey) {
    try {
      var result =
          cloudinaryConfig
              .cloudinary()
              .api()
              .deleteResources(Collections.singleton(mediaKey), ObjectUtils.emptyMap());
      return "deleted".equals(result.get("deleted"));
    } catch (Exception e) {
      throw new RuntimeException("Failed to delete image: " + mediaKey, e);
    }
  }
}
