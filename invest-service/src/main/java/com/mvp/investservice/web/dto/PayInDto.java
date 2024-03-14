package com.mvp.investservice.web.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Используется для пополнения счёта аккаунта
 */

@Data
public class PayInDto {

    private String accountId;

    private BigDecimal moneyToPay;
}
