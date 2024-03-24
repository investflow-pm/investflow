package com.example.reportservice.model.portfolio;

import lombok.Data;

import java.util.List;

@Data
public class PortfolioResponse {
    private List<Portfolio> portfolios;
}
