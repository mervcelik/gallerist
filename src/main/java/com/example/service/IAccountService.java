package com.example.service;

import com.example.dto.DtoAccount;
import com.example.dto.DtoAccountIU;

import java.util.List;

public interface IAccountService {

    public DtoAccount saveAccount(DtoAccountIU dtoAccountIU);
    public DtoAccount updateAccount(Long id,DtoAccountIU dtoAccountIU);
    public  void deleteAccount(Long id);
    public  DtoAccount findAccountById(Long id);
    public List<DtoAccount> findAccountAll();
}
