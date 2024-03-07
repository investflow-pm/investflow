package com.mvp.investservice.web.dto;

import lombok.Data;

@Data
public class StockDto {

    /*
        TODO подумать, какие ещё поля
         могут пригодится для акций
     */

    private String ticker;
    private String figi;
    private String name;
    private String sector;
    private String currency;
    private String countryOfRiskName;
    private int lots;
    private int sharesPerLot;
}
