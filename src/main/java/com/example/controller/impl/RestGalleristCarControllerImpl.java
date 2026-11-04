package com.example.controller.impl;

import com.example.controller.IRestGalleristCarController;
import com.example.controller.RestBaseController;
import com.example.controller.RootEntity;
import com.example.dto.DtoGalleristCar;
import com.example.dto.DtoGalleristCarIU;
import com.example.service.IGalleristCarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/gallerist-car")
public class RestGalleristCarControllerImpl extends RestBaseController implements IRestGalleristCarController {

    @Autowired
    private IGalleristCarService galleristCarService;

    @PostMapping("/save")
    @Override
    public RootEntity<DtoGalleristCar> save(@Valid @RequestBody DtoGalleristCarIU dtoGalleristCarIU) {
        return ok(galleristCarService.save(dtoGalleristCarIU));
    }

    @GetMapping(path = "/list")
    @Override
    public RootEntity<List<DtoGalleristCar>> findGalleristCarAll() {
        return ok(galleristCarService.findGalleristCarAll());
    }

    @GetMapping(path = "/list/{id}")
    @Override
    public RootEntity<DtoGalleristCar> findGalleristCarById(@PathVariable Long id) {
        return ok(galleristCarService.findGalleristCarById(id));
    }

    @PutMapping(path = "/update/{id}")
    @Override
    public RootEntity<DtoGalleristCar> updateGalleristCar(@PathVariable Long id, @Valid @RequestBody DtoGalleristCarIU dtoGalleristCarIU) {
        return ok(galleristCarService.updateGalleristCar(id, dtoGalleristCarIU));
    }

    @DeleteMapping(path = "/delete/{id}")
    @Override
    public void deleteGalleristCar(@PathVariable Long id) {
        galleristCarService.deleteGalleristCar(id);
    }
}
