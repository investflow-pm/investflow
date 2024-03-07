package com.mvp.investservice.service;

import com.mvp.investservice.web.dto.StockDto;

public interface StockService {

    StockDto getStockByName(String name);
}
