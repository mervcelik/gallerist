package com.example.controller;

import com.example.dto.DtoSaledCar;
import com.example.dto.DtoSaledCarIU;

import java.util.List;

public interface IRestSaledCarController {

    RootEntity<DtoSaledCar> buyCar(DtoSaledCarIU dtoSaledCarIU);

    RootEntity<List<DtoSaledCar>> findSaledCarAll();

    RootEntity<DtoSaledCar> findSaledCarById(Long id);
}
