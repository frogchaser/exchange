package com.company.vending.exchange.repository;

import com.company.vending.exchange.properties.CoinConfig;
import com.company.vending.exchange.domain.CoinType;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ExchangeCoinsRespository {
    private CoinConfig coinConfig;
    private Map<CoinType, Integer> coins;

    public ExchangeCoinsRespository(CoinConfig coinConfig) {
        this.coinConfig = coinConfig;
        this.coins = new HashMap<>();
        for (var coin : coinConfig.getInitial().entrySet()) {
            this.coins.put(coin.getKey(), coin.getValue());
        }
    }

    public Integer getCoinsByType(CoinType coinType) {
        return coins.get(coinType);
    }

    public Map<CoinType, Integer> getAllCoins() {
        Map<CoinType, Integer> allCoins = new HashMap<>();
        for (var coin : coins.entrySet()) {
            allCoins.put(coin.getKey(), coin.getValue());
        }
        return allCoins;
    }

    public void deductCoinsByType(CoinType coinType, Integer coinValue) {
        this.coins.put(coinType, this.coins.get(coinType) - coinValue);
    }

    public void resetCoins() {
        this.coins = new HashMap<>();
        for (var coin : this.coinConfig.getInitial().entrySet()) {
            this.coins.put(coin.getKey(), coin.getValue());
        }
    }
}
