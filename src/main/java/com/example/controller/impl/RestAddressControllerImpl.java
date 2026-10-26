package com.example.controller.impl;

import com.example.controller.IRestAddressController;
import com.example.controller.RestBaseController;
import com.example.controller.RootEntity;
import com.example.dto.DtoAddress;
import com.example.dto.DtoAddressIU;
import com.example.service.IAddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/address")
public class RestAddressControllerImpl extends RestBaseController implements IRestAddressController {

    @Autowired
    private IAddressService addressService;


    @PostMapping("/save")
    @Override
    public RootEntity<DtoAddress> saveAddress(@Valid @RequestBody DtoAddressIU addressUI) {
        return ok(addressService.saveAddress(addressUI));
    }

    @PutMapping(path = "/update/{id}")
    @Override
    public RootEntity<DtoAddress> updateAddress(@PathVariable Long id, @Valid @RequestBody DtoAddressIU dtoAddressIU) {
        return ok(addressService.updateAddress(id, dtoAddressIU));
    }

    @DeleteMapping(path = "/delete/{id}")
    @Override
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }

    @GetMapping(path = "/list/{id}")
    @Override
    public RootEntity<DtoAddress> findAddressById(@PathVariable Long id) {
        return ok(addressService.findAddressById(id));
    }

    @GetMapping(path = "/list")
    @Override
    public RootEntity<List<DtoAddress>> findAddressAll() {
        return ok(addressService.findAddressAll());
    }
}
