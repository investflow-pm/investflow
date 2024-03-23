package com.mvp.investservice.web.dto;

import lombok.Data;
import ru.tinkoff.piapi.contract.v1.OrderDirection;

@Data
public class SaleDto {
    private String accountId;

    private String figi;

    private int lot;

    private OrderType orderType;
}
