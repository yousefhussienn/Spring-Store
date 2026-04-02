package com.yh.springstore.service;

import java.util.List;

import com.yh.springstore.payload.AddressDTO;

public interface AddressService {

    AddressDTO createAddress(AddressDTO address);

    List<AddressDTO> getAllAddresses();

    List<AddressDTO> getAddressForLoggedInUser();

}
