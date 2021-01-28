package com.company.vending.exchange.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BaseResponse {
    String status;
}
