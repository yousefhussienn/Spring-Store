package com.yh.springstore.service;

import java.util.List;

import com.yh.springstore.payload.AddressDTO;

public interface AddressService {

    AddressDTO createAddress(AddressDTO address);

    List<AddressDTO> getAllAddresses();

    List<AddressDTO> getAddressForLoggedInUser();

    AddressDTO getAddressById(Long addressId);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    AddressDTO deleteAddress(Long addressId);

}
