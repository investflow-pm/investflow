package com.mvp.investservice.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockDto {

    @NotNull(message = "Id must not be null")
    private Long id;

    @NotNull(message = "Ticker must not be null")
    private String ticker;

    @NotNull(message = "FIGI must not be null")
    private String figi;

    @NotNull(message = "Name must not be null")
    private String name;

    private String sector;

    private String currency;

    private String countryOfRiskName;

    private Long lot;
}
