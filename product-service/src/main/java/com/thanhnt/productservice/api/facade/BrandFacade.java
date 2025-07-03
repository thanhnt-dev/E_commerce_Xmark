package com.thanhnt.productservice.api.facade;

import com.thanhnt.productservice.api.response.BaseResponse;
import com.thanhnt.productservice.api.response.BrandResponse;
import java.util.List;

public interface BrandFacade {
  BaseResponse<List<BrandResponse>> getAllBrands();
}
