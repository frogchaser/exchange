package com.company.vending.exchange.properties;

import com.company.vending.exchange.domain.CoinType;
import com.company.vending.exchange.domain.CurrencyType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "exchange")
public class CoinConfig {
    Map<CoinType, Integer> initial;
    CurrencyType currencyType;
}
