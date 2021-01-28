package com.company.vending.exchange.proxy;

import com.company.vending.exchange.BaseTest;
import com.company.vending.exchange.domain.CurrencyType;
import com.company.vending.exchange.proxy.domain.RatesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExchangeRatesProxyTest extends BaseTest {

    @Autowired
    private WebClient.Builder webClientBuilder;

    private WebClient webClient;

    private ExchangeRatesProxy exchangeRatesProxy;

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s/rates",
                                       mockBackEnd.getPort());
        webClient = webClientBuilder.clone()
                .baseUrl(baseUrl)
                .build();
        exchangeRatesProxy = new ExchangeRatesProxy(webClient, List.of("USD", "CAD", "GBP"));

    }

    @Test
    public void testSuccessResponse() throws JsonProcessingException {
        RatesResponse mockRateResponse = RatesResponse.builder()
                .rates(Map.of(CurrencyType.USD, 1.0,
                              CurrencyType.CAD, 1.2872384418,
                              CurrencyType.GBP, 0.7328012571))
                .base(CurrencyType.USD)
                .date(new Date())
                .build();
        mockBackEnd.enqueue(new MockResponse()
                                    .setBody(objectMapper.writeValueAsString(mockRateResponse))
                                    .addHeader("Content-Type", "application/json"));
        Mono<RatesResponse> actualResponse = exchangeRatesProxy.getExchangeRates(CurrencyType.USD);

        StepVerifier.create(actualResponse)
                .assertNext(ratesResponse -> {
                    assertEquals(CurrencyType.USD, ratesResponse.getBase());
                })
                .verifyComplete();
    }
}
