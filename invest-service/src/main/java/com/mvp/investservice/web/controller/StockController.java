package com.mvp.investservice.web.controller;


import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/invest/accounts/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    /*
        TODO
         Описать ручки по получению всех акций,
          определённой акции по имени, тикеру,
          покупки акции, продажи)
    */

    @GetMapping
    public StockDto getStockByName(@RequestParam String name) {
        return stockService.getStockByName(name);
    }

}
