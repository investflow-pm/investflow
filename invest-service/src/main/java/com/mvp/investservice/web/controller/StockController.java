package com.mvp.investservice.web.controller;


import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/{accountId}")
    public List<StockDto> getAllUserStocks(@PathVariable String accountId) {
        return stockService.getAllUserStocks(accountId);
    }

}
