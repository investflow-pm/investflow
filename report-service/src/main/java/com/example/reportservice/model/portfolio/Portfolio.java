package com.example.reportservice.model.portfolio;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class Portfolio {
    private String accountId;

    private BigDecimal expectedYield;

    private BigDecimal totalAmountShares;

    private BigDecimal totalAmountBonds;

    private BigDecimal totalAmountCurrencies;

    private BigDecimal totalAmountPortfolio;

    List<PositionResponse> positions;
}
