package com.example.service;

import com.example.dto.DtoCurrencyRate;
import com.example.dto.DtoCurrencyRates;

public interface ICurrencyRatesService {

    DtoCurrencyRates getCurrencyRates();

    DtoCurrencyRate getCurrencyRate(String currencyCode);
}
