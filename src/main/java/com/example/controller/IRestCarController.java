package com.example.controller;

import com.example.dto.DtoCar;
import com.example.dto.DtoCarIU;

import java.util.List;

public interface IRestCarController {

    RootEntity<DtoCar> save(DtoCarIU dtoCarIU);

    RootEntity<List<DtoCar>> findCarAll();

    RootEntity<DtoCar> findCarById(Long id);

    RootEntity<DtoCar> updateCar(Long id, DtoCarIU dtoCarIU);

    void deleteCar(Long id);
}
