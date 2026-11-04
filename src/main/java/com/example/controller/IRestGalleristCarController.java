package com.example.controller;

import com.example.dto.DtoGalleristCar;
import com.example.dto.DtoGalleristCarIU;

import java.util.List;

public interface IRestGalleristCarController {

    RootEntity<DtoGalleristCar> save(DtoGalleristCarIU dtoGalleristCarIU);

    RootEntity<List<DtoGalleristCar>> findGalleristCarAll();

    RootEntity<DtoGalleristCar> findGalleristCarById(Long id);

    RootEntity<DtoGalleristCar> updateGalleristCar(Long id, DtoGalleristCarIU dtoGalleristCarIU);

    void deleteGalleristCar(Long id);
}
