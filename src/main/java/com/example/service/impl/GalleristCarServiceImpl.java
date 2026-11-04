package com.example.service.impl;

import com.example.dto.DtoAddress;
import com.example.dto.DtoCar;
import com.example.dto.DtoGallerist;
import com.example.dto.DtoGalleristCar;
import com.example.dto.DtoGalleristCarIU;
import com.example.exception.BaseException;
import com.example.exception.ErrorMessage;
import com.example.exception.MessageType;
import com.example.model.Car;
import com.example.model.Gallerist;
import com.example.model.GalleristCar;
import com.example.repository.CarRepository;
import com.example.repository.GalleristCarRepository;
import com.example.repository.GalleristRepository;
import com.example.service.IGalleristCarService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GalleristCarServiceImpl implements IGalleristCarService {

    @Autowired
    private GalleristCarRepository galleristCarRepository;

    @Autowired
    private GalleristRepository galleristRepository;

    @Autowired
    private CarRepository carRepository;


    @Override
    public DtoGalleristCar save(DtoGalleristCarIU dtoGalleristCarIU) {

        if (galleristCarRepository.existsByGalleristIdAndCarId(dtoGalleristCarIU.getGalleristId(), dtoGalleristCarIU.getCarId())) {
            throw new BaseException(new ErrorMessage(MessageType.RECORD_ALREADY_EXIST,
                    "galleristId=" + dtoGalleristCarIU.getGalleristId() + ", carId=" + dtoGalleristCarIU.getCarId()));
        }

        GalleristCar galleristCar = createGalleristCar(dtoGalleristCarIU);

        GalleristCar savedGalleristCar = galleristCarRepository.save(galleristCar);

        return toDto(savedGalleristCar);
    }

    @Override
    public List<DtoGalleristCar> findGalleristCarAll() {
        List<GalleristCar> galleristCars = galleristCarRepository.findAll();

        List<DtoGalleristCar> dtoGalleristCars = new ArrayList<>();

        for (GalleristCar galleristCar : galleristCars) {
            dtoGalleristCars.add(toDto(galleristCar));
        }

        return dtoGalleristCars;
    }

    @Override
    public DtoGalleristCar findGalleristCarById(Long id) {
        GalleristCar galleristCar = findGalleristCarEntity(id);

        return toDto(galleristCar);
    }

    @Override
    public DtoGalleristCar updateGalleristCar(Long id, DtoGalleristCarIU dtoGalleristCarIU) {
        GalleristCar galleristCar = findGalleristCarEntity(id);

        if (galleristCarRepository.existsByGalleristIdAndCarIdAndIdNot(dtoGalleristCarIU.getGalleristId(), dtoGalleristCarIU.getCarId(), id)) {
            throw new BaseException(new ErrorMessage(MessageType.RECORD_ALREADY_EXIST,
                    "galleristId=" + dtoGalleristCarIU.getGalleristId() + ", carId=" + dtoGalleristCarIU.getCarId()));
        }

        galleristCar.setGallerist(findGallerist(dtoGalleristCarIU.getGalleristId()));
        galleristCar.setCar(findCar(dtoGalleristCarIU.getCarId()));

        GalleristCar updatedGalleristCar = galleristCarRepository.save(galleristCar);

        return toDto(updatedGalleristCar);
    }

    @Override
    public void deleteGalleristCar(Long id) {
        GalleristCar galleristCar = findGalleristCarEntity(id);

        galleristCarRepository.delete(galleristCar);
    }

    private GalleristCar createGalleristCar(DtoGalleristCarIU dtoGalleristCarIU) {

        Gallerist gallerist = findGallerist(dtoGalleristCarIU.getGalleristId());
        Car car = findCar(dtoGalleristCarIU.getCarId());

        GalleristCar galleristCar = new GalleristCar();

        galleristCar.setGallerist(gallerist);
        galleristCar.setCar(car);
        galleristCar.setCreateTime(new Date());

        return galleristCar;
    }

    private GalleristCar findGalleristCarEntity(Long id) {
        Optional<GalleristCar> optional = galleristCarRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }
        return optional.get();
    }

    private Gallerist findGallerist(Long galleristId) {
        Optional<Gallerist> optional = galleristRepository.findById(galleristId);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,galleristId.toString()));
        }
        return optional.get();
    }

    private Car findCar(Long carId) {
        Optional<Car> optional = carRepository.findById(carId);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,carId.toString()));
        }
        return optional.get();
    }

    private DtoGalleristCar toDto(GalleristCar galleristCar) {
        DtoGalleristCar dtoGalleristCar = new DtoGalleristCar();
        BeanUtils.copyProperties(galleristCar, dtoGalleristCar);

        Gallerist gallerist = galleristCar.getGallerist();
        if (gallerist != null) {
            DtoGallerist dtoGallerist = new DtoGallerist();
            BeanUtils.copyProperties(gallerist, dtoGallerist);

            if (gallerist.getAddress() != null) {
                DtoAddress dtoAddress = new DtoAddress();
                BeanUtils.copyProperties(gallerist.getAddress(), dtoAddress);
                dtoGallerist.setAddress(dtoAddress);
            }

            dtoGalleristCar.setGallerist(dtoGallerist);
        }

        if (galleristCar.getCar() != null) {
            DtoCar dtoCar = new DtoCar();
            BeanUtils.copyProperties(galleristCar.getCar(), dtoCar);
            dtoGalleristCar.setCar(dtoCar);
        }

        return dtoGalleristCar;
    }

}
