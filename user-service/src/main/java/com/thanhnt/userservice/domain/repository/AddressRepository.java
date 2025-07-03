package com.thanhnt.userservice.domain.repository;

import com.thanhnt.userservice.domain.entity.address.Address;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  Optional<Address> findById(Long id);
}
