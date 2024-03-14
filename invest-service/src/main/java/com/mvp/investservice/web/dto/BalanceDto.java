package com.mvp.investservice.web.dto;

import lombok.Data;

/**
 * Дтошка, возвращающая баланс аккаунта
 * и сумму пополнения
 */

@Data
public class BalanceDto {

    private String balance;

    private String addedMoney;
}
