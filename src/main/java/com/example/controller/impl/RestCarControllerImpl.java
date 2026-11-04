package com.example.controller.impl;

import com.example.controller.IRestCarController;
import com.example.controller.RestBaseController;
import com.example.controller.RootEntity;
import com.example.dto.DtoCar;
import com.example.dto.DtoCarIU;
import com.example.service.ICarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/car")
public class RestCarControllerImpl extends RestBaseController implements IRestCarController {

    @Autowired
    private ICarService carService;

    @PostMapping("/save")
    @Override
    public RootEntity<DtoCar> save(@Valid @RequestBody DtoCarIU dtoCarIU) {
        return ok(carService.save(dtoCarIU));
    }

    @GetMapping(path = "/list")
    @Override
    public RootEntity<List<DtoCar>> findCarAll() {
        return ok(carService.findCarAll());
    }

    @GetMapping(path = "/list/{id}")
    @Override
    public RootEntity<DtoCar> findCarById(@PathVariable Long id) {
        return ok(carService.findCarById(id));
    }

    @PutMapping(path = "/update/{id}")
    @Override
    public RootEntity<DtoCar> updateCar(@PathVariable Long id, @Valid @RequestBody DtoCarIU dtoCarIU) {
        return ok(carService.updateCar(id, dtoCarIU));
    }

    @DeleteMapping(path = "/delete/{id}")
    @Override
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}
