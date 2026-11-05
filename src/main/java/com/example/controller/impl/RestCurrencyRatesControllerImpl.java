package com.example.controller.impl;

import com.example.controller.IRestCurrencyRatesController;
import com.example.controller.RestBaseController;
import com.example.controller.RootEntity;
import com.example.dto.DtoCurrencyRate;
import com.example.dto.DtoCurrencyRates;
import com.example.service.ICurrencyRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/api/currency-rates")
public class RestCurrencyRatesControllerImpl extends RestBaseController implements IRestCurrencyRatesController {

    @Autowired
    private ICurrencyRatesService currencyRatesService;

    @GetMapping
    @Override
    public RootEntity<DtoCurrencyRates> getCurrencyRates() {
        return ok(currencyRatesService.getCurrencyRates());
    }

    @GetMapping(path = "/{currencyCode}")
    @Override
    public RootEntity<DtoCurrencyRate> getCurrencyRate(@PathVariable String currencyCode) {
        return ok(currencyRatesService.getCurrencyRate(currencyCode));
    }
}
