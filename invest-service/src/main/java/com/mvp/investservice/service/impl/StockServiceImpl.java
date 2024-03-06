package com.mvp.investservice.service.impl;

import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.Portfolio;
import ru.tinkoff.piapi.core.models.Position;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final InvestApi investApi;

    @Override
    public List<StockDto> getAllUserStocks(String accountId) {
        Portfolio portfolio = investApi.getOperationsService().getPortfolioSync(accountId);
        List<Position> positions = portfolio.getPositions();

        /*
        TODO Получать с аккаунта(портфолио) список позиций,
         искать из них Акции, кастить к нашей dtoшке и возвращать списком
        */

        return null;
    }
}
