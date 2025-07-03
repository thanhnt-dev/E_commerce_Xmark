package com.thanhnt.productservice.infrastructure.service;

import com.thanhnt.productservice.application.service.ProductLogService;
import com.thanhnt.productservice.domain.logs.LogDetail;
import com.thanhnt.productservice.domain.repository.LogDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductLogServiceImpl implements ProductLogService {
  private final LogDetailRepository logDetailRepository;

  @Override
  public void saveProductLog(LogDetail logDetail) {
    logDetailRepository.save(logDetail);
  }
}
