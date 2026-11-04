package com.example.controller;

import com.example.dto.DtoGallerist;
import com.example.dto.DtoGalleristIU;

import java.util.List;

public interface IRestGalleristController {

    RootEntity<DtoGallerist> save(DtoGalleristIU dtoGalleristIU);

    RootEntity<List<DtoGallerist>> findGalleristAll();

    RootEntity<DtoGallerist> findGalleristById(Long id);

    RootEntity<DtoGallerist> updateGallerist(Long id, DtoGalleristIU dtoGalleristIU);

    void deleteGallerist(Long id);
}
