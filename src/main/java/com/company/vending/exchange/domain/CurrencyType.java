package com.company.vending.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
public enum CurrencyType {

    USD("USD", "UNITED STATES", "DOLLAR"),
    CAD("CAD", "CANADA", "DOLLAR"),
    GBP("GBP", "UNITED KINGDOM", "POUND");

    private String code;
    private String countryName;
    private String currencyName;


}
