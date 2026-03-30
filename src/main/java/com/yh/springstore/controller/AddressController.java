package com.yh.springstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/user/address")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO address) {
        AddressDTO savedAddressDTO = addressService.createAddress(address);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    
    

}
