package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoCurrencyRate {

    private String currencyCode;

    private String name;

    private Integer unit;

    private BigDecimal forexBuying;

    private BigDecimal forexSelling;

    private BigDecimal banknoteBuying;

    private BigDecimal banknoteSelling;
}
