package com.example.controller;

import com.example.dto.DtoAccount;
import com.example.dto.DtoAccountIU;

import java.util.List;

public interface IRestAccountController {

    public  RootEntity<DtoAccount> saveAccount(DtoAccountIU dtoAccount);
    public RootEntity<DtoAccount> updateAccount(Long id,DtoAccountIU dtoAccountIU);
    public  void deleteAccount(Long id);
    public  RootEntity<DtoAccount> findAccountById(Long id);
    public RootEntity<List<DtoAccount>> findAccountAll();
}
