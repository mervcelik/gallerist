package com.example.service.impl;

import com.example.dto.DtoAddress;
import com.example.dto.DtoAddressIU;
import com.example.exception.BaseException;
import com.example.exception.ErrorMessage;
import com.example.exception.MessageType;
import com.example.model.Address;
import com.example.repository.AddressRepository;
import com.example.service.IAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public DtoAddress saveAddress(DtoAddressIU addressUI) {

        DtoAddress dtoAddress= new DtoAddress();
        Address saved = addressRepository.save(createAddress(addressUI));

        BeanUtils.copyProperties(saved,dtoAddress);
        return  dtoAddress;
    }

    @Override
    public DtoAddress updateAddress(Long id, DtoAddressIU dtoAddressIU) {
        DtoAddress dtoAddress= new DtoAddress();
        Optional<Address> optional = addressRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }
        Address address=optional.get();
        BeanUtils.copyProperties(dtoAddressIU,address);
        addressRepository.save(address);
        BeanUtils.copyProperties(address,dtoAddress);

        return dtoAddress;
    }

    @Override
    public void deleteAddress(Long id) {
        Optional<Address> optional = addressRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }
      addressRepository.delete(optional.get());
    }

    @Override
    public DtoAddress findAddressById(Long id) {
        Optional<Address> optional = addressRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }

        DtoAddress dtoAddress= new DtoAddress();
        BeanUtils.copyProperties(optional.get(),dtoAddress);

        return dtoAddress;
    }

    @Override
    public List<DtoAddress> findAddressAll() {
        List<Address> addresses = addressRepository.findAll();

        List<DtoAddress> dtoAddresses = new ArrayList<>();

        for (Address address : addresses) {
            DtoAddress dtoAddress = new DtoAddress();

            BeanUtils.copyProperties(address, dtoAddress);

            dtoAddresses.add(dtoAddress);
        }

        return dtoAddresses;
    }

    private Address createAddress(DtoAddressIU dtoAddressIU) {
        Address address = new Address();

        BeanUtils.copyProperties(dtoAddressIU, address);
        address.setCreateTime(new Date());
        return address;

    }
}
