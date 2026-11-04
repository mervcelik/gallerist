package com.example.service.impl;

import com.example.dto.DtoCar;
import com.example.dto.DtoCarIU;
import com.example.exception.BaseException;
import com.example.exception.ErrorMessage;
import com.example.exception.MessageType;
import com.example.model.Car;
import com.example.repository.CarRepository;
import com.example.service.ICarService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements ICarService {

    @Autowired
    private CarRepository carRepository;


    @Override
    public DtoCar save(DtoCarIU dtoCarIU) {

        Car car = createCar(dtoCarIU);

        Car savedCar = carRepository.save(car);

        return toDto(savedCar);
    }

    @Override
    public List<DtoCar> findCarAll() {
        List<Car> cars = carRepository.findAll();

        List<DtoCar> dtoCars = new ArrayList<>();

        for (Car car : cars) {
            dtoCars.add(toDto(car));
        }

        return dtoCars;
    }

    @Override
    public DtoCar findCarById(Long id) {
        Car car = findCarEntity(id);

        return toDto(car);
    }

    @Override
    public DtoCar updateCar(Long id, DtoCarIU dtoCarIU) {
        Car car = findCarEntity(id);

        BeanUtils.copyProperties(dtoCarIU, car);

        Car updatedCar = carRepository.save(car);

        return toDto(updatedCar);
    }

    @Override
    public void deleteCar(Long id) {
        Car car = findCarEntity(id);

        carRepository.delete(car);
    }

    private Car createCar(DtoCarIU dtoCarIU) {

        Car car = new Car();

        BeanUtils.copyProperties(dtoCarIU, car);

        car.setCreateTime(new Date());

        return car;
    }

    private Car findCarEntity(Long id) {
        Optional<Car> optional = carRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }
        return optional.get();
    }

    private DtoCar toDto(Car car) {
        DtoCar dtoCar = new DtoCar();
        BeanUtils.copyProperties(car, dtoCar);

        return dtoCar;
    }

}
