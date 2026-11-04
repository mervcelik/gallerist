package com.example.service;

import com.example.dto.DtoCar;
import com.example.dto.DtoCarIU;

import java.util.List;

public interface ICarService {

    DtoCar save(DtoCarIU dtoCarIU);

    List<DtoCar> findCarAll();

    DtoCar findCarById(Long id);

    DtoCar updateCar(Long id, DtoCarIU dtoCarIU);

    void deleteCar(Long id);
}
