package com.company.vending.exchange.validator;

import com.company.vending.exchange.domain.ExchangeCoinsRequest;
import com.company.vending.exchange.exception.RequestValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestValidator {

    private List<Integer> acceptedAmount;
    private List<String> acceptedCurrencyCode;

    public RequestValidator(@Value("${validator.amount}") List<Integer> acceptedAmount,
                            @Value("${validator.currency-code}") List<String> acceptedCurrencyCode) {
        this.acceptedAmount = acceptedAmount;
        this.acceptedCurrencyCode = acceptedCurrencyCode;
    }

    public void validateExchangeCoinRequest(ExchangeCoinsRequest exchangeCoinsRequest) throws RequestValidationException {
        validateAmount(exchangeCoinsRequest.getAmount());
        validateCurrencyCode(exchangeCoinsRequest.getCurrencyCode());
    }

    private void validateCurrencyCode(String currencyCode) throws RequestValidationException {
        if (!this.acceptedCurrencyCode.contains(currencyCode)) {
            throw new RequestValidationException("currencyCode is not valid");
        }
    }

    private void validateAmount(Integer amount) throws RequestValidationException {
        if (!this.acceptedAmount.contains(amount)) {
            throw new RequestValidationException("amount is not valid");
        }
    }
}
