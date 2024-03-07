package com.mvp.investservice.web.controller;

import com.mvp.investservice.service.PortfolioService;
import com.mvp.investservice.web.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invest/portfolios")
@RequiredArgsConstructor
public class PortfolioController {


    private final PortfolioService portfolioService;

    @GetMapping("/{accountId}")
    public List<StockDto> getAllUserStocks(@PathVariable String accountId) {
        return portfolioService.getAllUserStocks(accountId);
    }

    @GetMapping("/stocks")
    public StockDto getUserStockByName(@RequestParam String stockName) {

        return portfolioService.getUserStockByName(stockName);
    }
}
