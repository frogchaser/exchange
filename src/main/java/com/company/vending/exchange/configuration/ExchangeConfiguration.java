package com.company.vending.exchange.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ExchangeConfiguration {

    @Bean
    public WebClient exchangeRatesWebClient(WebClient.Builder webclientBuilder,
                                            @Value("${rates.base-uri}") String baseUri) {
        return webclientBuilder
                .clone()
                .baseUrl(baseUri)
                .build();
    }
}
