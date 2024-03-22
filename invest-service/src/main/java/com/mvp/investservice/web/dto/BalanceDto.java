package com.mvp.investservice.web.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Дтошка, возвращающая баланс аккаунта
 * и сумму пополнения
 */

@Data
public class BalanceDto {

    private BigDecimal balance;

    private BigDecimal addedMoney;
}
