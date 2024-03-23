package com.mvp.investservice.web.controller;


import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.SaleDto;
import com.mvp.investservice.web.dto.bond.BondDto;
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
    public List<StockDto> getStocksByName(@RequestParam(value = "name") String stockName) {
        return stockService.getStocksByName(stockName);
    }

    @GetMapping("/all")
    public List<StockDto> getAllStocks(@RequestParam(value = "page") Integer page,
                                        @RequestParam(value = "count") Integer count) {
        return stockService.getStocks(page, count);
    }

    @GetMapping("/sector/{sectorName}")
    public List<StockDto> getStocksBySector(@PathVariable String sectorName,
                                            @RequestParam(value = "count") Integer count) {
        return stockService.getStocksBySector(sectorName, count);
    }

    @PostMapping("/buy")
    public OrderResponse<StockDto> buyStock(@RequestBody PurchaseDto purchaseDto) {
        return stockService.buyStock(purchaseDto);
    }

    @PostMapping("/sale")
    public OrderResponse<StockDto> saleStock(@RequestBody SaleDto saleDto) {
        return stockService.saleStock(saleDto);
    }
}