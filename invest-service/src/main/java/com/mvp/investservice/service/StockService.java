package com.mvp.investservice.service;

import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.StockDto;

import java.util.List;

public interface StockService {

    StockDto getStockByName(String name);

    List<StockDto> getStocks();

    List<StockDto> getStocksBySector(String sectorName);

    OrderResponse<StockDto> buyStock(PurchaseDto purchaseDto);
}
