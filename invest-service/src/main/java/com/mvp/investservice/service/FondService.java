package com.mvp.investservice.service;

import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.fond.FondDto;
import com.mvp.investservice.web.dto.stock.StockDto;

import java.util.List;

public interface FondService {
    List<FondDto> getFondByName(String name);

    List<FondDto> getFonds(Integer page, Integer count);

    OrderResponse<FondDto> buyFond (PurchaseDto purchaseDto);
}
