package com.example.reportservice.controller;

import com.example.reportservice.model.portfolio.PortfolioRequest;
import com.example.reportservice.model.portfolio.PortfolioResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
@RequestMapping("/api/v1/report/portfolio")
@AllArgsConstructor
public class PositionController {
    private final RestTemplate restTemplate;

    @PostMapping
    public PortfolioResponse getPortfolio(@RequestBody PortfolioRequest portfolioRequest) {
        PortfolioResponse response = restTemplate
                .postForObject("http://localhost:9098/api/v1/invest/portfolios", portfolioRequest, PortfolioResponse.class);
        return response;
    }
}

