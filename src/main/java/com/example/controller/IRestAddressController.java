package com.example.controller;

import com.example.dto.DtoAddress;
import com.example.dto.DtoAddressUI;

public interface IRestAddressController {

    public RootEntity<DtoAddress> saveAddress(DtoAddressUI addressUI);

}
