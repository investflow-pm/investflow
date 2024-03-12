package com.mvp.investservice.service;

import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.StockDto;
import ru.tinkoff.piapi.contract.v1.PostOrderRequest;
import ru.tinkoff.piapi.contract.v1.PostOrderResponse;

import java.util.List;

public interface StockService {

    StockDto getStockByName(String name);

    List<StockDto> getStocks();

    List<StockDto> getStocksBySector(String sectorName);

    OrderResponse buyStock(PurchaseDto purchaseDto);
}
