package com.mvp.investservice.web.dto;

import lombok.Data;

@Data
public class PortfolioRequest {

    private String accountId;

    private String currency;
}
