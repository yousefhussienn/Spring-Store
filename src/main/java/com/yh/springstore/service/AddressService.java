package com.yh.springstore.service;

import java.util.List;

import com.yh.springstore.payload.AddressDTO;

import jakarta.validation.Valid;

public interface AddressService {

    AddressDTO createAddress(AddressDTO address);

    List<AddressDTO> getAllAddresses();

    List<AddressDTO> getAddressForLoggedInUser();

    AddressDTO getAddressById(Long addressId);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

}
