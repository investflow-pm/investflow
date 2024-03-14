package com.mvp.investservice.web.controller;

import com.mvp.investservice.service.PortfolioService;
import com.mvp.investservice.web.dto.portfolio.PortfolioRequest;
import com.mvp.investservice.web.dto.portfolio.PortfolioResponse;
import com.mvp.investservice.web.dto.portfolio.PositionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invest/portfolios")
@RequiredArgsConstructor
public class PortfolioController {


    private final PortfolioService portfolioService;

    @PostMapping
    public PortfolioResponse getUserPortfolio(@RequestBody PortfolioRequest portfolioRequest) {
        return portfolioService.getPortfolio(portfolioRequest);
    }

    @PostMapping("/positions")
    public List<PositionResponse> getUserPositions(@RequestBody PortfolioRequest portfolioRequest) {
        return portfolioService.getPortfolioPositions(portfolioRequest);
    }
}
