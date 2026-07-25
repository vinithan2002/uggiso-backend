package com.uggiso.uggiso_backend.repository;

import com.uggiso.uggiso_backend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);

    List<Address> findByCityIgnoreCase(String city);

    List<Address> findByDefaultAddress(Boolean defaultAddress);
}