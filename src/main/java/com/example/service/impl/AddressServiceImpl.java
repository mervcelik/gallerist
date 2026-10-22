package com.example.service.impl;

import com.example.dto.DtoAddress;
import com.example.dto.DtoAddressUI;
import com.example.model.Address;
import com.example.repository.AddressRepository;
import com.example.service.IAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public DtoAddress saveAddress(DtoAddressUI addressUI) {

        DtoAddress dtoAddress= new DtoAddress();
        Address saved = addressRepository.save(createAddress(addressUI));

        BeanUtils.copyProperties(saved,dtoAddress);
        return  dtoAddress;
    }

    private Address createAddress(DtoAddressUI dtoAddressUI) {
        Address address = new Address();

        BeanUtils.copyProperties(dtoAddressUI, address);
        address.setCreateTime(new Date());
        return address;

    }
}
