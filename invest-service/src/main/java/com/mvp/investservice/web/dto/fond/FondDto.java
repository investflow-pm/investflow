package com.mvp.investservice.web.dto.fond;

import lombok.Data;

@Data
public class FondDto {
    private String ticker;

    private String figi;

    private String name;

  //  private String sector;

    private String currency;

    private String countryOfRiskName;

    private int lots;
}
