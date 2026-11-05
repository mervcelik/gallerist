package com.example.service.impl;

import com.example.dto.DtoAccount;
import com.example.dto.DtoAddress;
import com.example.dto.DtoCar;
import com.example.dto.DtoCurrencyRate;
import com.example.dto.DtoCustomer;
import com.example.dto.DtoGallerist;
import com.example.dto.DtoSaledCar;
import com.example.dto.DtoSaledCarIU;
import com.example.enums.CarStatusType;
import com.example.enums.CurrencyType;
import com.example.exception.BaseException;
import com.example.exception.ErrorMessage;
import com.example.exception.MessageType;
import com.example.model.Account;
import com.example.model.Address;
import com.example.model.Car;
import com.example.model.Customer;
import com.example.model.Gallerist;
import com.example.model.SaledCar;
import com.example.repository.AccountRepository;
import com.example.repository.CarRepository;
import com.example.repository.CustomerRepository;
import com.example.repository.GalleristCarRepository;
import com.example.repository.GalleristRepository;
import com.example.repository.SaledCarRepository;
import com.example.service.ICurrencyRatesService;
import com.example.service.ISaledCarService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SaledCarServiceImpl implements ISaledCarService {

    @Autowired
    private SaledCarRepository saledCarRepository;

    @Autowired
    private GalleristRepository galleristRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GalleristCarRepository galleristCarRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ICurrencyRatesService currencyRatesService;


    @Override
    @Transactional
    public DtoSaledCar buyCar(DtoSaledCarIU dtoSaledCarIU) {

        Gallerist gallerist = findGallerist(dtoSaledCarIU.getGalleristId());
        Car car = findCar(dtoSaledCarIU.getCarId());
        Customer customer = findCustomer(dtoSaledCarIU.getCustomerId());

        if (!galleristCarRepository.existsByGalleristIdAndCarId(gallerist.getId(), car.getId())) {
            throw new BaseException(new ErrorMessage(MessageType.CAR_NOT_IN_GALLERIST,
                    "galleristId=" + gallerist.getId() + ", carId=" + car.getId()));
        }

        if (car.getCarStatusType() == CarStatusType.SALED) {
            throw new BaseException(new ErrorMessage(MessageType.CAR_STATUS_IS_ALREADY_SALED, car.getId().toString()));
        }

        Account account = customer.getAccount();
        if (account == null) {
            throw new BaseException(new ErrorMessage(MessageType.CUSTOMER_HAS_NO_ACCOUNT, customer.getId().toString()));
        }

        BigDecimal paidAmount = convert(car.getPrice(), car.getCurrencyType(), account.getCurrencyType());

        if (account.getAmount().compareTo(paidAmount) < 0) {
            throw new BaseException(new ErrorMessage(MessageType.CUSTOMER_AMOUNT_IS_NOT_ENOUGH,
                    "gereken=" + paidAmount + " " + account.getCurrencyType() + ", bakiye=" + account.getAmount() + " " + account.getCurrencyType()));
        }

        account.setAmount(account.getAmount().subtract(paidAmount));
        accountRepository.save(account);

        car.setCarStatusType(CarStatusType.SALED);
        carRepository.save(car);

        SaledCar saledCar = new SaledCar();
        saledCar.setGallerist(gallerist);
        saledCar.setCar(car);
        saledCar.setCustomer(customer);
        saledCar.setPaidAmount(paidAmount);
        saledCar.setPaidCurrencyType(account.getCurrencyType());
        saledCar.setExchangeRate(paidAmount.divide(car.getPrice(), 4, RoundingMode.HALF_UP));
        saledCar.setCreateTime(new Date());

        SaledCar savedSaledCar = saledCarRepository.save(saledCar);

        return toDto(savedSaledCar);
    }

    @Override
    public List<DtoSaledCar> findSaledCarAll() {
        List<SaledCar> saledCars = saledCarRepository.findAll();

        List<DtoSaledCar> dtoSaledCars = new ArrayList<>();

        for (SaledCar saledCar : saledCars) {
            dtoSaledCars.add(toDto(saledCar));
        }

        return dtoSaledCars;
    }

    @Override
    public DtoSaledCar findSaledCarById(Long id) {
        Optional<SaledCar> optional = saledCarRepository.findById(id);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,id.toString()));
        }
        return toDto(optional.get());
    }

    /**
     * Converts an amount between currencies through TRY using TCMB rates.
     * Foreign currency the customer has to buy uses the selling rate; foreign currency
     * the customer pays with uses the buying rate.
     */
    private BigDecimal convert(BigDecimal amount, CurrencyType from, CurrencyType to) {
        if (from == to) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal amountInTL = amount.multiply(rateToTL(from, true));

        return amountInTL.divide(rateToTL(to, false), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal rateToTL(CurrencyType currencyType, boolean selling) {
        if (currencyType == CurrencyType.TL) {
            return BigDecimal.ONE;
        }

        DtoCurrencyRate rate = currencyRatesService.getCurrencyRate(currencyType.name());
        BigDecimal value = selling ? rate.getForexSelling() : rate.getForexBuying();
        if (value == null) {
            throw new BaseException(new ErrorMessage(MessageType.CURRENCY_RATES_NOT_AVAILABLE, currencyType.name()));
        }

        return value.divide(BigDecimal.valueOf(rate.getUnit()), 6, RoundingMode.HALF_UP);
    }

    private Gallerist findGallerist(Long galleristId) {
        Optional<Gallerist> optional = galleristRepository.findById(galleristId);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,galleristId.toString()));
        }
        return optional.get();
    }

    private Car findCar(Long carId) {
        Optional<Car> optional = carRepository.findById(carId);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,carId.toString()));
        }
        return optional.get();
    }

    private Customer findCustomer(Long customerId) {
        Optional<Customer> optional = customerRepository.findById(customerId);
        if(optional.isEmpty()){
            throw  new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,customerId.toString()));
        }
        return optional.get();
    }

    private DtoSaledCar toDto(SaledCar saledCar) {
        DtoSaledCar dtoSaledCar = new DtoSaledCar();
        BeanUtils.copyProperties(saledCar, dtoSaledCar);

        if (saledCar.getGallerist() != null) {
            Gallerist gallerist = saledCar.getGallerist();
            DtoGallerist dtoGallerist = new DtoGallerist();
            BeanUtils.copyProperties(gallerist, dtoGallerist);
            dtoGallerist.setAddress(toDto(gallerist.getAddress()));
            dtoSaledCar.setGallerist(dtoGallerist);
        }

        if (saledCar.getCar() != null) {
            DtoCar dtoCar = new DtoCar();
            BeanUtils.copyProperties(saledCar.getCar(), dtoCar);
            dtoSaledCar.setCar(dtoCar);
        }

        if (saledCar.getCustomer() != null) {
            Customer customer = saledCar.getCustomer();
            DtoCustomer dtoCustomer = new DtoCustomer();
            BeanUtils.copyProperties(customer, dtoCustomer);
            dtoCustomer.setAddress(toDto(customer.getAddress()));

            if (customer.getAccount() != null) {
                DtoAccount dtoAccount = new DtoAccount();
                BeanUtils.copyProperties(customer.getAccount(), dtoAccount);
                dtoCustomer.setAccount(dtoAccount);
            }

            dtoSaledCar.setCustomer(dtoCustomer);
        }

        return dtoSaledCar;
    }

    private DtoAddress toDto(Address address) {
        if (address == null) {
            return null;
        }
        DtoAddress dtoAddress = new DtoAddress();
        BeanUtils.copyProperties(address, dtoAddress);
        return dtoAddress;
    }

}
