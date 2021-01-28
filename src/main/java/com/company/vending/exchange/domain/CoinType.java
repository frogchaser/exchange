package com.company.vending.exchange.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@ToString
@RequiredArgsConstructor
public enum CoinType {
    ONE("ONE", new BigDecimal(0.01).setScale(2, RoundingMode.FLOOR)),
    FIVE("FIVE", new BigDecimal(0.05).setScale(2, RoundingMode.FLOOR)),
    TEN("TEN", new BigDecimal(0.10).setScale(2, RoundingMode.FLOOR)),
    TWENTY_FIVE("TWENTY_FIVE", new BigDecimal(0.25).setScale(2, RoundingMode.FLOOR));

    private final String name;
    private final BigDecimal value;
}
