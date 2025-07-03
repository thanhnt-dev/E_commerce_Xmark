package com.thanhnt.userservice.application.service;

import com.thanhnt.userservice.domain.entity.address.Address;

public interface AddressService {
  Address findById(Long id);

  void updateAddress(Address address);
}
