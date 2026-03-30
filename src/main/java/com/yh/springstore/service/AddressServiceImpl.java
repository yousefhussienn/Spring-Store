package com.yh.springstore.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yh.springstore.exception.APIException;
import com.yh.springstore.model.Address;
import com.yh.springstore.payload.AddressDTO;
import com.yh.springstore.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO address) {
        // Check if not already exists
        if (addressRepository.findById(address.getAddressId()) != null)
            throw new APIException("Address is already exists!");

        // Map the incoming AddressDTO to a Address entity
        Address newAddress = modelMapper.map(address, Address.class);
        addressRepository.save(newAddress);

        // Map the saved Address entity back to a DTO and return
        AddressDTO savedAddress = modelMapper.map(newAddress, AddressDTO.class);
        return savedAddress;
    }

}
