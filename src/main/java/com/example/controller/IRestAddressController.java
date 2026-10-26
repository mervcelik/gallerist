package com.example.controller;

import com.example.dto.DtoAddress;
import com.example.dto.DtoAddressIU;

import java.util.List;

public interface IRestAddressController {

     RootEntity<DtoAddress> saveAddress(DtoAddressIU addressUI);
     RootEntity<DtoAddress> updateAddress(Long id, DtoAddressIU dtoAddressIU);

     void deleteAddress(Long id);

     RootEntity<DtoAddress> findAddressById(Long id);

     RootEntity<List<DtoAddress>> findAddressAll();
}
