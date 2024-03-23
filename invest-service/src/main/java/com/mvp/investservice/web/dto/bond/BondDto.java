package com.mvp.investservice.web.dto.bond;

import com.mvp.investservice.web.dto.Asset;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BondDto extends Asset {

    private String ticker;

    private String figi;

    private String name;

    private int lots;

    private String currency;

    private int couponQuantityPerYear;

    private LocalDateTime maturityDate;

    private BigDecimal placementPrice;

    private String countryOfRiskName;

    private String sector;

    private RiskLevel riskLevel;
}
