package com.example.service;

import com.example.dto.DtoCustomer;
import com.example.dto.DtoCustomerIU;

import java.util.List;

public interface ICustomerService {

    DtoCustomer save(DtoCustomerIU dtoCustomerIU);

    List<DtoCustomer> findCustomerAll();

    DtoCustomer findCustomerById(Long id);

    DtoCustomer updateCustomer(Long id, DtoCustomerIU dtoCustomerIU);

    void deleteCustomer(Long id);
}
