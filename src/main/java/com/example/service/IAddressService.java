package com.example.service;

import com.example.dto.DtoAddress;
import com.example.dto.DtoAddressUI;

public interface IAddressService {

    public DtoAddress saveAddress(DtoAddressUI addressUI);
}
