package com.mvp.investservice.web.controller;

import com.mvp.investservice.service.FondService;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.fond.FondDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/invest/fonds")
@RequiredArgsConstructor
public class FondController {
    private final FondService fondService;

    @GetMapping
    public List<FondDto> getFondByName (@RequestParam(value = "name") String stockName) {
        return fondService.getFondByName(stockName);
    }

    @GetMapping("/all")
    public List<FondDto> getAllFonds(@RequestParam(value = "page") Integer page,
                                       @RequestParam(value = "count") Integer count) {
        return fondService.getFonds(page, count);
    }

    @PostMapping("/buy")
    public OrderResponse<FondDto> buyFond(@RequestBody PurchaseDto purchaseDto) {
        return fondService.buyFond(purchaseDto);
    }
    @PostMapping("/sell")
    public OrderResponse<FondDto> sellFond(@RequestBody PurchaseDto purchaseDto) {
        return fondService.sellFond(purchaseDto);
    }
}
