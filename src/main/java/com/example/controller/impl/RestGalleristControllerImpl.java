package com.example.controller.impl;

import com.example.controller.IRestGalleristController;
import com.example.controller.RestBaseController;
import com.example.controller.RootEntity;
import com.example.dto.DtoGallerist;
import com.example.dto.DtoGalleristIU;
import com.example.service.IGalleristService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/gallerist")
public class RestGalleristControllerImpl extends RestBaseController implements IRestGalleristController {

    @Autowired
    private IGalleristService galleristService;

    @PostMapping("/save")
    @Override
    public RootEntity<DtoGallerist> save(@Valid @RequestBody DtoGalleristIU dtoGalleristIU) {
        return ok(galleristService.save(dtoGalleristIU));
    }

    @GetMapping(path = "/list")
    @Override
    public RootEntity<List<DtoGallerist>> findGalleristAll() {
        return ok(galleristService.findGalleristAll());
    }

    @GetMapping(path = "/list/{id}")
    @Override
    public RootEntity<DtoGallerist> findGalleristById(@PathVariable Long id) {
        return ok(galleristService.findGalleristById(id));
    }

    @PutMapping(path = "/update/{id}")
    @Override
    public RootEntity<DtoGallerist> updateGallerist(@PathVariable Long id, @Valid @RequestBody DtoGalleristIU dtoGalleristIU) {
        return ok(galleristService.updateGallerist(id, dtoGalleristIU));
    }

    @DeleteMapping(path = "/delete/{id}")
    @Override
    public void deleteGallerist(@PathVariable Long id) {
        galleristService.deleteGallerist(id);
    }
}
