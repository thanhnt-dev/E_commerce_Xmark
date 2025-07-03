package com.thanhnt.productservice.infrastructure.facade;

import com.thanhnt.productservice.api.facade.BrandFacade;
import com.thanhnt.productservice.api.response.BaseResponse;
import com.thanhnt.productservice.api.response.BrandResponse;
import com.thanhnt.productservice.application.service.BrandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class BrandFacadeImpl implements BrandFacade {

  private final BrandService brandService;

  @Override
  public BaseResponse<List<BrandResponse>> getAllBrands() {
    log.info("Fetching all brands from the database");
    List<BrandResponse> brandResponses =
        brandService.findAll().stream()
            .map(
                brand ->
                    BrandResponse.builder()
                        .id(brand.getId())
                        .name(brand.getBrandName())
                        .description(brand.getDescription())
                        .logoUrl(brand.getLogoUrl())
                        .build())
            .toList();
    log.info("Successfully fetched {} brands", brandResponses.size());
    return BaseResponse.build(brandResponses, true);
  }
}
