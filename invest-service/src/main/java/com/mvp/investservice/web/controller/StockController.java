package com.mvp.investservice.web.controller;

import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.StockDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invest")
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockService stockService;

    @GetMapping("/stocks")
    public List<StockDto> getStocks() {
        return stockService.getAllStocks();
    }
}
