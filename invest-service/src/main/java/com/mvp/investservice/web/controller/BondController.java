package com.mvp.investservice.web.controller;

import com.mvp.investservice.service.BondService;
import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.bond.BondDto;
import com.mvp.investservice.web.dto.stock.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/invest/bonds")
@RequiredArgsConstructor
public class BondController {

    private final BondService bondService;

    @GetMapping
    public List<BondDto> getBondsByName(@RequestParam(value = "name") String bondName) {
        return bondService.getBondsByName(bondName);
    }

    @GetMapping("/all")
    public List<BondDto> getAllBonds(@RequestParam(value = "page") Integer page,
                                       @RequestParam(value = "count") Integer count) {
        return bondService.getBonds(page, count);
    }
//
//    @GetMapping("/sector/{sectorName}")
//    public List<StockDto> getStocksBySector(@PathVariable String sectorName) {
//        return bondService.getStocksBySector(sectorName);
//    }
//
//    @PostMapping("/buy")
//    public OrderResponse<StockDto> buyStock(@RequestBody PurchaseDto purchaseDto) {
//        return bondService.buyStock(purchaseDto);
//    }
}
