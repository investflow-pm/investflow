package com.mvp.investservice.web.controller;

import com.mvp.investservice.domain.exception.InsufficientFundsException;
import com.mvp.investservice.service.BondService;
import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.SaleDto;
import com.mvp.investservice.web.dto.bond.BondDto;
import com.mvp.investservice.web.dto.stock.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/sector/{sectorName}")
    public List<BondDto> getBondsBySector(@PathVariable String sectorName,
                                            @RequestParam(value = "count") Integer count) {
        return bondService.getBondsBySector(sectorName, count);
    }

    @PostMapping("/buy")
    public OrderResponse<BondDto> buyBond(@RequestBody PurchaseDto purchaseDto) {
        try {
            return bondService.buyBond(purchaseDto);
        }
        catch (InsufficientFundsException ex) {
            throw new InsufficientFundsException(ex.getAssetInfo());
        }
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<?> handleInsufficientFundsException(InsufficientFundsException ex) {
        record ExceptionBody(String message, Object body) {}
        return new ResponseEntity<>(new ExceptionBody(ex.getMessage(), ex.getAssetInfo()), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/sale")
    public OrderResponse<BondDto> saleBond(@RequestBody SaleDto saleDto) {
        return bondService.saleBond(saleDto);
    }
}
