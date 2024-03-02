package com.mvp.crudmicroservice.portfolio.web.controller;

import com.mvp.crudmicroservice.AssetService;
import com.mvp.crudmicroservice.stock.domain.Stock;
import com.mvp.crudmicroservice.stock.web.dto.StockDto;
import com.mvp.crudmicroservice.stock.web.mapper.StockMapper;
import com.mvp.crudmicroservice.user.domain.exception.StockAddingToUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/crud/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final AssetService<Stock> stockService;

    private final StockMapper stockMapper;

    @PostMapping("/{userId}/stocks")
    public ResponseEntity addStock(@PathVariable Long userId,
                                   @RequestBody StockDto stockDto,
                                   @RequestParam Long lots) {
        try {
            Stock stock = stockMapper.toEntity(stockDto);
            Stock addedStock = stockService.buyAsset(stock, userId, lots);
            return ResponseEntity.status(HttpStatus.CREATED).body(stockMapper.toDto(addedStock));
        } catch (StockAddingToUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
