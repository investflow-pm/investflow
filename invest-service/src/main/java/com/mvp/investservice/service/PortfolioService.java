package com.mvp.investservice.service;

import com.mvp.investservice.web.dto.PortfolioRequest;
import com.mvp.investservice.web.dto.PositionResponse;
import com.mvp.investservice.web.dto.StockDto;

import java.util.List;

public interface PortfolioService {

        List<PositionResponse> getPortfolioPositions(PortfolioRequest portfolioRequest);
}
