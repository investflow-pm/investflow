package com.mvp.investservice.web.dto;

import lombok.Data;

@Data
public class StockDto {

    private String ticker;
    private String figi;
    private String name;
    private String sector;
    private String currency;
    private String countryOfRiskName;
    private int lots;
}
