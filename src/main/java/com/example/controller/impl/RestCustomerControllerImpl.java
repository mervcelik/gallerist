package com.example.controller.impl;

import com.example.controller.IRestCustomerController;
import com.example.controller.RestBaseController;
import com.example.controller.RootEntity;
import com.example.dto.DtoCustomer;
import com.example.dto.DtoCustomerIU;
import com.example.service.ICustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/customer")
public class RestCustomerControllerImpl extends RestBaseController implements IRestCustomerController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping("/save")
    @Override
    public RootEntity<DtoCustomer> save(@Valid @RequestBody DtoCustomerIU dtoCustomerIU) {
        return RootEntity.ok(customerService.save(dtoCustomerIU));
    }

    @GetMapping(path = "/list")
    @Override
    public RootEntity<List<DtoCustomer>> findCustomerAll() {
        return ok(customerService.findCustomerAll());
    }

    @GetMapping(path = "/list/{id}")
    @Override
    public RootEntity<DtoCustomer> findCustomerById(@PathVariable Long id) {
        return ok(customerService.findCustomerById(id));
    }

    @PutMapping(path = "/update/{id}")
    @Override
    public RootEntity<DtoCustomer> updateCustomer(@PathVariable Long id, @Valid @RequestBody DtoCustomerIU dtoCustomerIU) {
        return ok(customerService.updateCustomer(id, dtoCustomerIU));
    }

    @DeleteMapping(path = "/delete/{id}")
    @Override
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}
