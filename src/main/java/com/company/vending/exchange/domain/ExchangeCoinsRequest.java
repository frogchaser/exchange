package com.company.vending.exchange.domain;

import lombok.Value;

@Value
public class ExchangeCoinsRequest {
    Integer amount;
    String currencyCode;
}
