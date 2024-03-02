package com.mvp.crudmicroservice.stock.service;

import com.mvp.crudmicroservice.stock.domain.Stock;
import com.mvp.crudmicroservice.stock.web.dto.StockDto;

import java.util.List;

public interface StockService {

    Stock add(Stock stock, Long userId, Long count);

    List<Stock> getAllByUserId(Long userId);
}
