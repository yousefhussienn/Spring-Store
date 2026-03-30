package com.yh.springstore.service;

import java.util.ArrayList;
import java.util.List;

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
    public AddressDTO createAddress(AddressDTO addressDTO) {
        // Check if id is sent
        if (addressDTO.getAddressId() != null)
            throw new APIException("ID should not be provided when creating an address");
        
        // Map the incoming DTO to an Address entity
        Address newAddress = modelMapper.map(addressDTO, Address.class);
        // Save to DB
        newAddress = addressRepository.save(newAddress);

        // Map back to DTO, and return
        AddressDTO savedAddress = modelMapper.map(newAddress, AddressDTO.class);
        return savedAddress;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        // Check if empty
        if (addressRepository.count() == 0)
            throw new APIException("No Addresses created yet!");

        // Get all address from DB and Map each to DTO
        List<AddressDTO> addressDTOs = new ArrayList<>();
        addressRepository.findAll().forEach(address -> {
            addressDTOs.add(modelMapper.map(address, AddressDTO.class));
        });

        return addressDTOs;
    }

}
