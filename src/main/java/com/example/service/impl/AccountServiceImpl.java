package com.example.service.impl;

import com.example.dto.DtoAccount;
import com.example.dto.DtoAccountIU;
import com.example.exception.BaseException;
import com.example.exception.ErrorMessage;
import com.example.exception.MessageType;
import com.example.model.Account;
import com.example.repository.AccountRepository;
import com.example.service.IAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl  implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public DtoAccount saveAccount(DtoAccountIU dtoAccountIU) {
        DtoAccount dtoAccount= new DtoAccount();
        Account saved = accountRepository.save(createAccount(dtoAccountIU));

        BeanUtils.copyProperties(saved,dtoAccount);
        return dtoAccount;
    }

    @Override
    public DtoAccount updateAccount(Long id, DtoAccountIU dtoAccountIU) {
        DtoAccount dtoAccount= new DtoAccount();
        Optional<Account> optional = accountRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }
        Account account=optional.get();
        BeanUtils.copyProperties(dtoAccountIU,account);
        accountRepository.save(account);
        BeanUtils.copyProperties(account,dtoAccount);

        return dtoAccount;
    }

    @Override
    public void deleteAccount(Long id) {
        Optional<Account> optional = accountRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }
        accountRepository.delete(optional.get());
    }

    @Override
    public DtoAccount findAccountById(Long id) {

        DtoAccount dtoAccount= new DtoAccount();

        Optional<Account> optional = accountRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }

        BeanUtils.copyProperties(optional.get(),dtoAccount);

        return dtoAccount;
    }

    @Override
    public List<DtoAccount> findAccountAll() {
        List<Account> accounts = accountRepository.findAll();

        List<DtoAccount> dtoAccounts = new ArrayList<>();

        for (Account account : accounts) {
            DtoAccount dtoAccount = new DtoAccount();

            BeanUtils.copyProperties(account, dtoAccount);

            dtoAccounts.add(dtoAccount);
        }

        return dtoAccounts;
    }

    private Account createAccount(DtoAccountIU dtoAccountIU) {
        Account account = new Account();
        BeanUtils.copyProperties(dtoAccountIU, account);
        account.setCreateTime(new Date());
        return account;
    }
}
