package com.mvp.investservice.service.impl;

import com.mvp.investservice.service.PortfolioService;
import com.mvp.investservice.web.dto.PortfolioRequest;
import com.mvp.investservice.web.dto.PositionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.Portfolio;
import ru.tinkoff.piapi.core.models.Position;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final InvestApi investApi;

    @Override
    public List<PositionResponse> getPortfolioPositions(PortfolioRequest portfolioRequest) {
        Portfolio portfolio = investApi.getOperationsService()
                .getPortfolioSync(portfolioRequest.getAccountId());

        List<Position> positions = portfolio.getPositions();

        return null;

    }
}
