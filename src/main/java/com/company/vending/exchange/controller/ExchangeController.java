package com.company.vending.exchange.controller;

import com.company.vending.exchange.domain.BaseResponse;
import com.company.vending.exchange.domain.ErrorInfo;
import com.company.vending.exchange.domain.ExchangeCoinsRequest;
import com.company.vending.exchange.domain.ExchangeCoinsResponse;
import com.company.vending.exchange.exception.InsufficientCoinsException;
import com.company.vending.exchange.exception.RequestValidationException;
import com.company.vending.exchange.service.ExchangeService;
import com.company.vending.exchange.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class ExchangeController {

    private RequestValidator requestValidator;
    private ExchangeService exchangeService;

    private final Logger logger = LoggerFactory.getLogger(ExchangeController.class);

    public ExchangeController(RequestValidator requestValidator, ExchangeService exchangeService) {
        this.requestValidator = requestValidator;
        this.exchangeService = exchangeService;
    }

    @PostMapping("/coins")
    public ExchangeCoinsResponse exchangeCoins(@RequestBody ExchangeCoinsRequest exchangeCoinsRequest)
            throws RequestValidationException, InsufficientCoinsException {

        requestValidator.validateExchangeCoinRequest(exchangeCoinsRequest);
        logger.info("Exchange amount: {}", exchangeCoinsRequest.getAmount());
        logger.info("Exchange currency code: {}", exchangeCoinsRequest.getCurrencyCode());
        return exchangeService.getExchangeCoins(exchangeCoinsRequest);
    }

    @GetMapping("/coins/reset")
    public BaseResponse resetCoins()
           {
        return exchangeService.resetToInitialCoins();
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<ErrorInfo> handleRequestValidationException(RequestValidationException requestValidationException) {
        return ResponseEntity.badRequest().body(new ErrorInfo(requestValidationException));
    }

    @ExceptionHandler(InsufficientCoinsException.class)
    public ResponseEntity<ErrorInfo> handleInsufficientCoinsException(InsufficientCoinsException insufficientCoinsException) {
        return ResponseEntity.badRequest().body(new ErrorInfo(insufficientCoinsException));
    }
}
