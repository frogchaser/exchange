package com.company.vending.exchange.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
public class ErrorInfo {
    private String errorMessage;
    private String errorCause;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date date;

    public ErrorInfo(Throwable throwable) {
        this.errorMessage = throwable.getLocalizedMessage();
        this.errorCause = throwable.getClass().getName();
        this.date = new Date();
    }
}
