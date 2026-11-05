package com.example.service;

import com.example.dto.DtoSaledCar;
import com.example.dto.DtoSaledCarIU;

import java.util.List;

public interface ISaledCarService {

    DtoSaledCar buyCar(DtoSaledCarIU dtoSaledCarIU);

    List<DtoSaledCar> findSaledCarAll();

    DtoSaledCar findSaledCarById(Long id);
}
