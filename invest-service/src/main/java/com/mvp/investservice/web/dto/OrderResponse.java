package com.mvp.investservice.web.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderResponse<A> {

    private String orderId;

    private String executionStatus;

    private int lotRequested;

    private int lotExecuted;

    private A asset;

    private BigDecimal initialOrderPrice;

    private BigDecimal executedOrderPrice;

    private BigDecimal totalOrderPrice;
}
