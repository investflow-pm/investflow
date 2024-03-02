package com.mvp.crudmicroservice.stock.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StockDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String ticker;
    private String figi;
    private String name;
    private String sector;
    private String currency;
    private String countryOfRiskName;
    private int sharesPerLot;

}
