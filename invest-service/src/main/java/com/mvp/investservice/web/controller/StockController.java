package com.mvp.investservice.web.controller;


import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.stock.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/invest/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public StockDto getStockByName(@RequestParam(value = "name") String stockName) {
        return stockService.getStockByName(stockName);
    }

    @GetMapping("/all")
    public List<StockDto> getAllStocks() {
        return stockService.getStocks();
    }

    @GetMapping("/sector/{sectorName}")
    public List<StockDto> getStocksBySector(@PathVariable String sectorName) {
        return stockService.getStocksBySector(sectorName);
    }

    @PostMapping("/buy")
    public OrderResponse<StockDto> buyStock(@RequestBody PurchaseDto purchaseDto) {
        return stockService.buyStock(purchaseDto);
    }
}