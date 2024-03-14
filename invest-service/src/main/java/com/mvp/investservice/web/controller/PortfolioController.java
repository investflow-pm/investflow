package com.mvp.investservice.web.controller;

import com.mvp.investservice.service.PortfolioService;
import com.mvp.investservice.web.dto.PortfolioRequest;
import com.mvp.investservice.web.dto.PositionResponse;
import com.mvp.investservice.web.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invest/portfolios")
@RequiredArgsConstructor
public class PortfolioController {


    private final PortfolioService portfolioService;

    @PostMapping
    public List<PositionResponse> getUserPositions(@RequestBody PortfolioRequest portfolioRequest) {
        return portfolioService.getPortfolioPositions(portfolioRequest);
    }
}
