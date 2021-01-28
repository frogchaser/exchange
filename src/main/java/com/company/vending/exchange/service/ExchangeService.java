package com.company.vending.exchange.service;

import com.company.vending.exchange.domain.*;
import com.company.vending.exchange.properties.CoinConfig;
import com.company.vending.exchange.exception.InsufficientCoinsException;
import com.company.vending.exchange.proxy.ExchangeRatesProxy;
import com.company.vending.exchange.proxy.domain.RatesResponse;
import com.company.vending.exchange.repository.ExchangeCoinsRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeService {

    private CoinConfig coinConfig;
    private ExchangeRatesProxy exchangeRatesProxy;
    private ExchangeCoinsRespository exchangeCoinsRespository;

    private final Logger logger = LoggerFactory.getLogger(ExchangeService.class);

    public ExchangeService(CoinConfig coinConfig, ExchangeRatesProxy exchangeRatesProxy,
                           ExchangeCoinsRespository exchangeCoinsRespository) {
        this.coinConfig = coinConfig;
        this.exchangeRatesProxy = exchangeRatesProxy;
        this.exchangeCoinsRespository = exchangeCoinsRespository;
    }


    public ExchangeCoinsResponse getExchangeCoins(ExchangeCoinsRequest exchangeCoinsRequest) throws InsufficientCoinsException {
        RatesResponse ratesResponse = exchangeRatesProxy.getExchangeRates(coinConfig.getCurrencyType()).block();
        Double rates = ratesResponse.getRates().get(CurrencyType.valueOf(exchangeCoinsRequest.getCurrencyCode()));
        BigDecimal amountRequested = new BigDecimal(exchangeCoinsRequest.getAmount() / rates)
                .setScale(2, RoundingMode.CEILING);
        Map<CoinType, Integer> coins = processCoins(amountRequested);
        return ExchangeCoinsResponse.builder().status("Success")
                .currencyCode(coinConfig.getCurrencyType().getCode())
                .amountDispensed(amountRequested)
                .coins(coins)
                .build();
    }

    private Map<CoinType, Integer> processCoins(BigDecimal amountRequested) throws InsufficientCoinsException {
        BigDecimal current = new BigDecimal(amountRequested.doubleValue()).setScale(2, RoundingMode.CEILING);
        Map<CoinType, Integer> coins = new HashMap<>();
        Integer availableTwentyFiveCoins = exchangeCoinsRespository.getCoinsByType(CoinType.TWENTY_FIVE);
        Integer twentyFiveCoins = getAvailableCount(current, CoinType.TWENTY_FIVE.getValue(), availableTwentyFiveCoins);
        BigDecimal twentyFiveTotal = multiply(new BigDecimal(twentyFiveCoins), CoinType.TWENTY_FIVE.getValue());
        coins.put(CoinType.TWENTY_FIVE, twentyFiveCoins);
        current = calculateCurrent(current, twentyFiveTotal);

        Integer availableTenCoins = exchangeCoinsRespository.getCoinsByType(CoinType.TEN);
        Integer tenCoins = getAvailableCount(current, CoinType.TEN.getValue(), availableTenCoins);
        BigDecimal tenTotal = multiply(new BigDecimal(tenCoins), CoinType.TEN.getValue());
        coins.put(CoinType.TEN, tenCoins);
        current = calculateCurrent(current, tenTotal);

        Integer availableFiveCoins = exchangeCoinsRespository.getCoinsByType(CoinType.FIVE);
        Integer fiveCoins = getAvailableCount(current, CoinType.FIVE.getValue(), availableFiveCoins);
        BigDecimal fiveTotal = multiply(new BigDecimal(fiveCoins), CoinType.FIVE.getValue());
        coins.put(CoinType.FIVE, fiveCoins);
        current = calculateCurrent(current, fiveTotal);

        Integer availableOneCoins = exchangeCoinsRespository.getCoinsByType(CoinType.FIVE);
        Integer oneCoins = getAvailableCount(current, CoinType.ONE.getValue(), availableOneCoins);
        BigDecimal oneTotal = multiply(new BigDecimal(oneCoins), CoinType.ONE.getValue());
        coins.put(CoinType.ONE, oneCoins);
        current = calculateCurrent(current, oneTotal);

        if (current.doubleValue() > 0) {
            throw new InsufficientCoinsException("not enough coins available");
        }

        exchangeCoinsRespository.deductCoinsByType(CoinType.TWENTY_FIVE, twentyFiveCoins);
        exchangeCoinsRespository.deductCoinsByType(CoinType.TEN, tenCoins);
        exchangeCoinsRespository.deductCoinsByType(CoinType.FIVE, fiveCoins);
        exchangeCoinsRespository.deductCoinsByType(CoinType.ONE, oneCoins);
        return coins;
    }

    private BigDecimal calculateCurrent(BigDecimal current, BigDecimal coinTotal) {
        BigDecimal newCurrent = current.subtract(coinTotal).setScale(2, RoundingMode.CEILING);
        if (newCurrent.doubleValue() >= 0) {
            return newCurrent;
        } else {
            return current;
        }
    }


    private Integer getAvailableCount(BigDecimal current, BigDecimal value, Integer availableCoins) {
        if (current.doubleValue() <= 0) {
            return 0;
        }
        Integer newCoins = current.divide(value).intValue();
        if (newCoins <= availableCoins) {
            return newCoins;
        }
        return availableCoins;
    }

    private BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }


    public BaseResponse resetToInitialCoins() {
        exchangeCoinsRespository.resetCoins();
        return BaseResponse.builder().status("Success").build();
    }
}
