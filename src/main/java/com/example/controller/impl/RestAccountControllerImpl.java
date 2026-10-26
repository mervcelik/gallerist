package com.example.controller.impl;

import com.example.controller.IRestAccountController;
import com.example.controller.RestBaseController;
import com.example.controller.RootEntity;
import com.example.dto.DtoAccount;
import com.example.dto.DtoAccountIU;
import com.example.service.IAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/account")
public class RestAccountControllerImpl extends RestBaseController implements IRestAccountController
{
    @Autowired
    private IAccountService accountService;

    @PostMapping("/save")
    @Override
    public RootEntity<DtoAccount> saveAccount(@Valid @RequestBody DtoAccountIU dtoAccount) {
        return ok(accountService.saveAccount(dtoAccount));
    }
    @PutMapping(path = "/update/{id}")
    @Override
    public RootEntity<DtoAccount> updateAccount(@PathVariable Long id, @Valid @RequestBody DtoAccountIU dtoAccountIU) {
        return ok(accountService.updateAccount(id,dtoAccountIU));
    }

    @DeleteMapping(path = "/delete/{id}")
    @Override
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

    @GetMapping(path = "/list/{id}")
    @Override
    public RootEntity<DtoAccount> findAccountById(@PathVariable Long id) {
        return ok(accountService.findAccountById(id));
    }

    @GetMapping(path = "/list")
    @Override
    public RootEntity<List<DtoAccount>> findAccountAll() {
        return ok(accountService.findAccountAll());
    }
}
