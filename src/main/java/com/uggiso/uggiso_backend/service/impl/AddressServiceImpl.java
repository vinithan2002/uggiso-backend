package com.uggiso.uggiso_backend.service.impl;

import com.uggiso.uggiso_backend.dto.request.AddressRequest;
import com.uggiso.uggiso_backend.dto.response.AddressResponse;
import com.uggiso.uggiso_backend.entity.Address;
import com.uggiso.uggiso_backend.entity.Users;
import com.uggiso.uggiso_backend.repository.AddressRepository;
import com.uggiso.uggiso_backend.repository.UserDetailsRepository;
import com.uggiso.uggiso_backend.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserDetailsRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository,
                              UserDetailsRepository userRepository) {

        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AddressResponse createAddress(AddressRequest request) {

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Address address = new Address();

        address.setHouseNo(request.getHouseNo());
        address.setStreet(request.getStreet());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setAddressType(request.getAddressType());
        address.setDefaultAddress(request.getDefaultAddress());
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return convertToResponse(savedAddress);
    }

    @Override
    public AddressResponse getAddressById(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Address not found"));

        return convertToResponse(address);
    }

    @Override
    public List<AddressResponse> getAddressesByUser(Long userId) {

        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse updateAddress(Long id,
                                         AddressRequest request) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Address not found"));

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        address.setHouseNo(request.getHouseNo());
        address.setStreet(request.getStreet());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setAddressType(request.getAddressType());
        address.setDefaultAddress(request.getDefaultAddress());
        address.setUser(user);

        Address updatedAddress = addressRepository.save(address);

        return convertToResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Address not found"));

        addressRepository.delete(address);
    }

    private AddressResponse convertToResponse(Address address) {

        AddressResponse response = new AddressResponse();

        response.setId(address.getId());
        response.setHouseNo(address.getHouseNo());
        response.setStreet(address.getStreet());
        response.setLandmark(address.getLandmark());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPincode(address.getPincode());
        response.setAddressType(address.getAddressType());
        response.setDefaultAddress(address.getDefaultAddress());

        response.setUserId(address.getUser().getId());
        response.setUserName(address.getUser().getUsername());

        return response;
    }

}