package com.mvp.investservice.web.controller;


import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.piapi.contract.v1.PostOrderResponse;

import java.util.List;


@RestController
@RequestMapping("api/v1/invest/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/{stockName}")
    public StockDto getStockByName(@PathVariable String stockName) {
        return stockService.getStockByName(stockName);
    }

    @GetMapping()
    public List<StockDto> getAllStocks() {
        return stockService.getStocks();
    }

    @GetMapping("/sector/{sectorName}")
    public List<StockDto> getStocksBySector(@PathVariable String sectorName) {
        return stockService.getStocksBySector(sectorName);
    }

    @PostMapping("/buy")
    public OrderResponse buyStock(@RequestBody PurchaseDto purchaseDto) {
        return stockService.buyStock(purchaseDto);
    }
}