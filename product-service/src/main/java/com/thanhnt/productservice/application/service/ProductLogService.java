package com.thanhnt.productservice.application.service;

import com.thanhnt.productservice.domain.logs.LogDetail;

public interface ProductLogService {
  void saveProductLog(LogDetail logDetail);
}
