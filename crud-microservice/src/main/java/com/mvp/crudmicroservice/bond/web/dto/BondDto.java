package com.mvp.crudmicroservice.bond.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BondDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String ticker;
    private String figi;
    private String name;
    private String sector;
    private String currency;
    private String countryOfRiskName;
    private String logoName;
    // TODO: check coupon info
    //private String coupon;

}
