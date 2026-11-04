package com.example.service;

import com.example.dto.DtoGalleristCar;
import com.example.dto.DtoGalleristCarIU;

import java.util.List;

public interface IGalleristCarService {

    DtoGalleristCar save(DtoGalleristCarIU dtoGalleristCarIU);

    List<DtoGalleristCar> findGalleristCarAll();

    DtoGalleristCar findGalleristCarById(Long id);

    DtoGalleristCar updateGalleristCar(Long id, DtoGalleristCarIU dtoGalleristCarIU);

    void deleteGalleristCar(Long id);
}
