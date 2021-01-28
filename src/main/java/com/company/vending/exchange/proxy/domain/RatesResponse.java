package com.company.vending.exchange.proxy.domain;

import com.company.vending.exchange.domain.CurrencyType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class RatesResponse {
    private Map<CurrencyType, Double> rates;
    private CurrencyType base;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;
}
