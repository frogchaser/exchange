package com.company.vending.exchange.proxy;

import com.company.vending.exchange.domain.CurrencyType;
import com.company.vending.exchange.proxy.domain.RatesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ExchangeRatesProxy {

    private WebClient webClient;
    private List<String> symbols;
    private final static String EXCHANGE_RATES_PATH = "/latest";

    public ExchangeRatesProxy(WebClient exchangeRatesWebClient, @Value("${rates.symbols}") List<String> symbols) {
        this.webClient = exchangeRatesWebClient;
        this.symbols = symbols;
    }

    public Mono<RatesResponse> getExchangeRates(CurrencyType currencyType) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(EXCHANGE_RATES_PATH)
                        .queryParam("base", currencyType.getCode())
                        .queryParam("symbols", symbols)
                        .build())
                .retrieve()
                .bodyToMono(RatesResponse.class)
                .log();
    }

}
