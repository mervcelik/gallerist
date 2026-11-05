package com.example.dto;

import com.example.enums.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DtoSaledCar extends DtoBase {

    private DtoGallerist gallerist;

    private DtoCar car;

    private DtoCustomer customer;

    private BigDecimal paidAmount;

    private CurrencyType paidCurrencyType;

    private BigDecimal exchangeRate;
}
