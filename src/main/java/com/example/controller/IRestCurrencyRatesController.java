package com.example.controller;

import com.example.dto.DtoCurrencyRate;
import com.example.dto.DtoCurrencyRates;

public interface IRestCurrencyRatesController {

    RootEntity<DtoCurrencyRates> getCurrencyRates();

    RootEntity<DtoCurrencyRate> getCurrencyRate(String currencyCode);
}
