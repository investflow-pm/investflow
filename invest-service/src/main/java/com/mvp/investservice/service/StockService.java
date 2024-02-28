package com.mvp.investservice.service;

import com.mvp.investservice.domain.invest_items.shares.Stock;
import com.mvp.investservice.web.dto.StockDto;

import java.util.List;

public interface StockService {

    List<StockDto> getAllStocks();
}
