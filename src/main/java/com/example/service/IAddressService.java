package com.example.service;

import com.example.dto.DtoAddress;
import com.example.dto.DtoAddressIU;

import java.util.List;

public interface IAddressService {

    DtoAddress saveAddress(DtoAddressIU addressUI);

    DtoAddress updateAddress(Long id, DtoAddressIU dtoAddressIU);

    void deleteAddress(Long id);

    DtoAddress findAddressById(Long id);

    List<DtoAddress> findAddressAll();
}
