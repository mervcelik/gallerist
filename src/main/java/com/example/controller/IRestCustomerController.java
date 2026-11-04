package com.example.controller;

import com.example.dto.DtoCustomer;
import com.example.dto.DtoCustomerIU;

import java.util.List;

public interface IRestCustomerController {

    public RootEntity<DtoCustomer> save(DtoCustomerIU dtoCustomerIU);

    RootEntity<List<DtoCustomer>> findCustomerAll();

    RootEntity<DtoCustomer> findCustomerById(Long id);

    RootEntity<DtoCustomer> updateCustomer(Long id, DtoCustomerIU dtoCustomerIU);

    void deleteCustomer(Long id);
}
