package com.thanhnt.userservice.infrastructure.service;

import com.thanhnt.userservice.application.service.AddressService;
import com.thanhnt.userservice.domain.entity.address.Address;
import com.thanhnt.userservice.domain.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
  private final AddressRepository addressRepository;

  @Override
  public Address findById(Long id) {
    return addressRepository.findById(id).orElse(null);
  }

  @Override
  @Transactional
  public void updateAddress(Address address) {
    addressRepository.save(address);
  }
}
