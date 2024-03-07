package com.mvp.investservice.service.impl;

import com.mvp.investservice.service.PortfolioService;
import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.StockDto;
import com.mvp.investservice.web.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.PortfolioPosition;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final InvestApi investApi;

    private final StockService stockService;

    private final StockMapper stockMapper;

    @Override
    public List<StockDto> getAllUserStocks(String accountId) {
        List<PortfolioPosition> portfolioPositions = investApi
                .getSandboxService()
                .getPortfolioSync(accountId).getPositionsList();

        List<PortfolioPosition> shares = portfolioPositions.stream()
                .filter(e -> e.getInstrumentType().equals("share"))
                .toList();

        return getSharesDto(shares);
    }

    @Override
    public StockDto getUserStockByName(String stockName) {

        return null;
    }

    private List<PortfolioPosition> getUserShares(String accountId) {
        List<PortfolioPosition> portfolioPositions = investApi
                .getSandboxService()
                .getPortfolioSync(accountId).getPositionsList();

        List<PortfolioPosition> positions = portfolioPositions.stream()
                .filter(e -> e.getInstrumentType().equals("share"))
                .toList();

        return positions;
    }

    private List<StockDto> getSharesDto(List<PortfolioPosition> shares) {
        List<String> shareFigies = shares.stream()
                .map(PortfolioPosition::getFigi).toList();

        List<Share> userShares = shareFigies.stream()
                .map(figi -> investApi.getInstrumentsService().getShareByFigiSync(figi))
                .toList();

        return userShares.stream().map(stockMapper::toDto).toList();
    }
}
