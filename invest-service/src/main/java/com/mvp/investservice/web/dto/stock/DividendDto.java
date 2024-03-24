package com.mvp.investservice.web.dto.stock;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DividendDto {
    private LocalDateTime date; // дата

    private BigDecimal paymentPerShare; // выплата на одну акцию

    private String currency; // валюта

    private BigDecimal interestIncome; // процентный доход
}
