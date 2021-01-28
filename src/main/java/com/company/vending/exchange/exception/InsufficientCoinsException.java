package com.company.vending.exchange.exception;

public class InsufficientCoinsException extends Exception {
    public InsufficientCoinsException(String message) {
        super(message);
    }
}
