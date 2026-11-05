package com.example.controller.impl;

import com.example.controller.IRestSaledCarController;
import com.example.controller.RestBaseController;
import com.example.controller.RootEntity;
import com.example.dto.DtoSaledCar;
import com.example.dto.DtoSaledCarIU;
import com.example.service.ISaledCarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/saled-car")
public class RestSaledCarControllerImpl extends RestBaseController implements IRestSaledCarController {

    @Autowired
    private ISaledCarService saledCarService;

    @PostMapping("/save")
    @Override
    public RootEntity<DtoSaledCar> buyCar(@Valid @RequestBody DtoSaledCarIU dtoSaledCarIU) {
        return ok(saledCarService.buyCar(dtoSaledCarIU));
    }

    @GetMapping(path = "/list")
    @Override
    public RootEntity<List<DtoSaledCar>> findSaledCarAll() {
        return ok(saledCarService.findSaledCarAll());
    }

    @GetMapping(path = "/list/{id}")
    @Override
    public RootEntity<DtoSaledCar> findSaledCarById(@PathVariable Long id) {
        return ok(saledCarService.findSaledCarById(id));
    }
}
