package com.yh.springstore.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yh.springstore.exception.APIException;
import com.yh.springstore.exception.ResourceNotFoundException;
import com.yh.springstore.model.Address;
import com.yh.springstore.model.User;
import com.yh.springstore.payload.AddressDTO;
import com.yh.springstore.repository.AddressRepository;
import com.yh.springstore.repository.UserRepository;
import com.yh.springstore.util.AuthUtil;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        // Make sure address id is not sent
        if (addressDTO.getAddressId() != null)
            throw new APIException("ID should not be provided when creating an address");
        
        // Map the incoming DTO to an Address entity
        Address newAddress = modelMapper.map(addressDTO, Address.class);

        // Get Loggedin User
        User loggedInUser = authUtil.loggedInUser();
        
        // Set address to the user 
        loggedInUser.addAddress(newAddress);

        // Save both Address and User to DB
        newAddress = addressRepository.save(newAddress);
        loggedInUser = userRepository.save(loggedInUser);

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

    @Override
    public List<AddressDTO> getAddressForLoggedInUser() {
        // Get Addresses for logged in user
        List<Address> addresses = addressRepository.findByUserEmail(authUtil.loggedInEmail());
       
        // Map List to DTOs
        List<AddressDTO> addressDTOs = new ArrayList<>();
        addresses.forEach(address -> {
            addressDTOs.add(modelMapper.map(address, AddressDTO.class));
        });

        return addressDTOs;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));        
        return modelMapper.map(address, AddressDTO.class);
    }

}
