package com.thanhnt.productservice.application.service;

import java.util.Map;

public interface CloudinaryService {
  Map<String, String> uploadImage(byte[] image);

  boolean deleteImage(String mediaKey);

  String getImageUrl(String assetKey);
}
