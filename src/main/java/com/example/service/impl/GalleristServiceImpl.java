package com.example.service.impl;

import com.example.dto.DtoAddress;
import com.example.dto.DtoGallerist;
import com.example.dto.DtoGalleristIU;
import com.example.exception.BaseException;
import com.example.exception.ErrorMessage;
import com.example.exception.MessageType;
import com.example.model.Address;
import com.example.model.Gallerist;
import com.example.repository.AddressRepository;
import com.example.repository.GalleristRepository;
import com.example.service.IGalleristService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GalleristServiceImpl implements IGalleristService {

    @Autowired
    private GalleristRepository galleristRepository;

    @Autowired
    private AddressRepository addressRepository;


    @Override
    public DtoGallerist save(DtoGalleristIU dtoGalleristIU) {

        Gallerist gallerist = createGallerist(dtoGalleristIU);

        Gallerist savedGallerist = galleristRepository.save(gallerist);

        return toDto(savedGallerist);
    }

    @Override
    public List<DtoGallerist> findGalleristAll() {
        List<Gallerist> gallerists = galleristRepository.findAll();

        List<DtoGallerist> dtoGallerists = new ArrayList<>();

        for (Gallerist gallerist : gallerists) {
            dtoGallerists.add(toDto(gallerist));
        }

        return dtoGallerists;
    }

    @Override
    public DtoGallerist findGalleristById(Long id) {
        Gallerist gallerist = findGalleristEntity(id);

        return toDto(gallerist);
    }

    @Override
    public DtoGallerist updateGallerist(Long id, DtoGalleristIU dtoGalleristIU) {
        Gallerist gallerist = findGalleristEntity(id);

        BeanUtils.copyProperties(dtoGalleristIU, gallerist);

        gallerist.setAddress(findAddress(dtoGalleristIU.getAddressId()));

        Gallerist updatedGallerist = galleristRepository.save(gallerist);

        return toDto(updatedGallerist);
    }

    @Override
    public void deleteGallerist(Long id) {
        Gallerist gallerist = findGalleristEntity(id);

        galleristRepository.delete(gallerist);
    }

    private Gallerist createGallerist(DtoGalleristIU dtoGalleristIU) {

        Address address = findAddress(dtoGalleristIU.getAddressId());

        Gallerist gallerist = new Gallerist();

        BeanUtils.copyProperties(dtoGalleristIU, gallerist);

        gallerist.setAddress(address);
        gallerist.setCreateTime(new Date());

        return gallerist;
    }

    private Gallerist findGalleristEntity(Long id) {
        Optional<Gallerist> optional = galleristRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }
        return optional.get();
    }

    private Address findAddress(Long addressId) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if(optionalAddress.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,addressId.toString()));
        }
        return optionalAddress.get();
    }

    private DtoGallerist toDto(Gallerist gallerist) {
        DtoGallerist dtoGallerist = new DtoGallerist();
        BeanUtils.copyProperties(gallerist, dtoGallerist);

        if (gallerist.getAddress() != null) {
            DtoAddress dtoAddress = new DtoAddress();
            BeanUtils.copyProperties(gallerist.getAddress(), dtoAddress);
            dtoGallerist.setAddress(dtoAddress);
        }

        return dtoGallerist;
    }

}
