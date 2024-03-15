package com.mvp.investservice.web.dto.portfolio;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawMoney {

    private String currency;

    private BigDecimal amount;
}
