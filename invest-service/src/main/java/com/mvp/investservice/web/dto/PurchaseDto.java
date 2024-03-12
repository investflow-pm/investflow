package com.mvp.investservice.web.dto;

import lombok.Data;

/**
 * Покупка актива
 */

@Data
public class PurchaseDto {
    private String accountId;

    private String figi;

    private int lot;

    private OrderType orderType;
}
