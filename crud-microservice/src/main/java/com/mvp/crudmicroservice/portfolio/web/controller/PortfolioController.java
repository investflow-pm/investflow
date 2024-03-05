package com.mvp.crudmicroservice.portfolio.web.controller;

import com.mvp.crudmicroservice.AssetService;
import com.mvp.crudmicroservice.bond.domain.Bond;
import com.mvp.crudmicroservice.bond.web.dto.BondDto;
import com.mvp.crudmicroservice.bond.web.mapper.BondMapper;
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

    private final AssetService<Bond> bondService;
    private final BondMapper bondMapper;

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

    // TODO: change lots to quantity?
    @PostMapping("/{userId}/bonds")
    public ResponseEntity addBond(@PathVariable Long userId,
                                  @RequestBody BondDto bondDto,
                                  @RequestParam Long lots) {
        try {
            var bond = bondMapper.toEntity(bondDto);
            var addedBond = bondService.buyAsset(bond, userId, lots);
            return ResponseEntity.status(HttpStatus.CREATED).body(bondMapper.toDto(addedBond));
        } catch (StockAddingToUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
