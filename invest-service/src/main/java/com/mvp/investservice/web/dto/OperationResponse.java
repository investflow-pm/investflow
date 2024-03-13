package com.mvp.investservice.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OperationResponse {

    private String operationId;

    private String currency;

    private BigDecimal payment;

    private BigDecimal price;

    private BigDecimal lotPrice;

    private String operationState;

    private String operationType;

    private String figi;

    private String assetName;

    private String instrumentType;

    private LocalDateTime operationDate;
}
