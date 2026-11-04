package com.example.service;

import com.example.dto.DtoGallerist;
import com.example.dto.DtoGalleristIU;

import java.util.List;

public interface IGalleristService {

    DtoGallerist save(DtoGalleristIU dtoGalleristIU);

    List<DtoGallerist> findGalleristAll();

    DtoGallerist findGalleristById(Long id);

    DtoGallerist updateGallerist(Long id, DtoGalleristIU dtoGalleristIU);

    void deleteGallerist(Long id);
}
