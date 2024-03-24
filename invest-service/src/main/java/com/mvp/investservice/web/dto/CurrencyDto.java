package com.mvp.investservice.web.dto;

import lombok.Data;

@Data
public class CurrencyDto extends Asset {

    private String name;

    private String currency;

    private String figi;

    private String ticker;

    private String countryOfRiskName;

    private BrandLogoDto brandLogo;
}
