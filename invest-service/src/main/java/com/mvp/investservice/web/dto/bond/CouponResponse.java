package com.mvp.investservice.web.dto.bond;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class CouponResponse {
    private String figi;
    LocalDateTime nextPaymentDate; // ближайшая выплата
    Long couponsPaid; // выплачено купонов
    Long couponsCount; // всего купонов

    List<CouponDto> dividends;
}
