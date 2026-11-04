package com.example.service.impl;

import com.example.dto.DtoAccount;
import com.example.dto.DtoAddress;
import com.example.dto.DtoCustomer;
import com.example.dto.DtoCustomerIU;
import com.example.exception.BaseException;
import com.example.exception.ErrorMessage;
import com.example.exception.MessageType;
import com.example.model.Account;
import com.example.model.Address;
import com.example.model.Customer;
import com.example.repository.AccountRepository;
import com.example.repository.AddressRepository;
import com.example.repository.CustomerRepository;
import com.example.service.ICustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public DtoCustomer save(DtoCustomerIU dtoCustomerIU) {

        Customer customer = createCustomer(dtoCustomerIU);

        Customer savedCustomer = customerRepository.save(customer);

        return toDto(savedCustomer);
    }

    @Override
    public List<DtoCustomer> findCustomerAll() {
        List<Customer> customers = customerRepository.findAll();

        List<DtoCustomer> dtoCustomers = new ArrayList<>();

        for (Customer customer : customers) {
            dtoCustomers.add(toDto(customer));
        }

        return dtoCustomers;
    }

    @Override
    public DtoCustomer findCustomerById(Long id) {
        Customer customer = findCustomerEntity(id);

        return toDto(customer);
    }

    @Override
    public DtoCustomer updateCustomer(Long id, DtoCustomerIU dtoCustomerIU) {
        Customer customer = findCustomerEntity(id);

        BeanUtils.copyProperties(dtoCustomerIU, customer);

        customer.setAddress(findAddress(dtoCustomerIU.getAddressId()));
        customer.setAccount(findAccount(dtoCustomerIU.getAccountId()));

        Customer updatedCustomer = customerRepository.save(customer);

        return toDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = findCustomerEntity(id);

        customerRepository.delete(customer);
    }

    private Customer createCustomer (DtoCustomerIU dtoCustomerIU){

        Address address = findAddress(dtoCustomerIU.getAddressId());
        Account account = findAccount(dtoCustomerIU.getAccountId());

        Customer customer= new Customer();

        BeanUtils.copyProperties(dtoCustomerIU,customer);

        customer.setAccount(account);
        customer.setAddress(address);
        customer.setCreateTime(new Date());

        return customer;
    }

    private Customer findCustomerEntity(Long id) {
        Optional<Customer> optional = customerRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }
        return optional.get();
    }

    private Address findAddress(Long addressId) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if(optionalAddress.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,addressId.toString()));
        }
        return optionalAddress.get();
    }

    private Account findAccount(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if(accountOptional.isEmpty()){
            throw  new BaseException(new ErrorMessage( MessageType.NO_RECORD_EXIST,accountId.toString()));
        }
        return accountOptional.get();
    }

    private DtoCustomer toDto(Customer customer) {
        DtoCustomer dtoCustomer= new DtoCustomer();
        BeanUtils.copyProperties(customer,dtoCustomer);

        if (customer.getAccount() != null) {
            DtoAccount dtoAccount= new DtoAccount();
            BeanUtils.copyProperties(customer.getAccount(),dtoAccount);
            dtoCustomer.setAccount(dtoAccount);
        }

        if (customer.getAddress() != null) {
            DtoAddress dtoAddress= new DtoAddress();
            BeanUtils.copyProperties(customer.getAddress(),dtoAddress);
            dtoCustomer.setAddress(dtoAddress);
        }

        return dtoCustomer;
    }

}
