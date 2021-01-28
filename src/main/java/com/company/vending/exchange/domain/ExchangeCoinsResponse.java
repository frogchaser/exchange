package com.company.vending.exchange.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder(toBuilder = true)
public class ExchangeCoinsResponse {
    String status;
    BigDecimal amountDispensed;
    String currencyCode;
    Map<CoinType, Integer> coins;
}
