package com.mvp.investservice.web.dto.stock;

import com.mvp.investservice.web.dto.Asset;
import lombok.Data;

@Data
public class StockDto extends Asset {

    private String ticker;
    private String figi;
    private String name;
    private String sector;
    private String currency;
    private String countryOfRiskName;
    private int lots;
}
