package com.uggiso.uggiso_backend.service;

import com.uggiso.uggiso_backend.dto.request.AddressRequest;
import com.uggiso.uggiso_backend.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse createAddress(AddressRequest request);

    AddressResponse getAddressById(Long id);

    List<AddressResponse> getAddressesByUser(Long userId);

    AddressResponse updateAddress(Long id, AddressRequest request);

    void deleteAddress(Long id);
}