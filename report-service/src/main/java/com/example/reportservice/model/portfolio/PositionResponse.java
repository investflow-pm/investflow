package com.example.reportservice.model.portfolio;


import com.example.reportservice.model.dto.Asset;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PositionResponse {

    private String instrumentType;

    private Asset asset;

    private BigDecimal averagePositionPrice;

    private BigDecimal expectedYield;

    private BigDecimal currentPrice;

    private String currency;

    private int quantity;

    private BigDecimal totalPrice;

}
