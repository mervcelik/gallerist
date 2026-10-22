package com.example.controller.impl;

import com.example.controller.IRestAddressController;
import com.example.controller.RestBaseController;
import com.example.controller.RootEntity;
import com.example.dto.DtoAddress;
import com.example.dto.DtoAddressUI;
import com.example.service.IAddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/api/address")
public class RestAddressControllerImpl extends RestBaseController implements IRestAddressController {

    @Autowired
    private IAddressService addressService;


    @PostMapping("/save")
    @Override
    public RootEntity<DtoAddress> saveAddress(@Valid @RequestBody DtoAddressUI addressUI) {
        return ok(addressService.saveAddress(addressUI));
    }
}
