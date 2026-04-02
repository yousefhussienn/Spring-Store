package com.yh.springstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yh.springstore.payload.AddressDTO;
import com.yh.springstore.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AddressService addressService;    

    @PostMapping("/users/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO address) {
        AddressDTO savedAddressDTO = addressService.createAddress(address);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addresses = addressService.getAllAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressForLoggedInUser() {
        List<AddressDTO> addresses = addressService.getAddressForLoggedInUser();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO address = addressService.getAddressById(addressId);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PutMapping("/users/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(
        @PathVariable Long addressId,
        @Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO savedAddressDTO = addressService.updateAddress(addressId, addressDTO);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }
    
}
