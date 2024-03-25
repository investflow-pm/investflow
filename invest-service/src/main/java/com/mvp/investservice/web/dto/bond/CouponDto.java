package com.mvp.investservice.web.dto.bond;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponDto {
    private LocalDateTime couponDate; // дата
    private BigDecimal accumulatedCouponIncome; // НКД
    private Long couponNumber; // номер купона
    private BigDecimal payment; // размер купона
    private String currency; // валюта
    private BigDecimal interestIncome; // процентный доход
}
