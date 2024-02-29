package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.CannotProceedApiRequestException;
import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.StockDto;
import com.mvp.investservice.web.mappers.impl.StockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final InvestApi investApi;

    private final StockMapper stockMapper;

    @Override
    public List<StockDto> getAllStocks() {
        log.info("Entering getAllStocks service method...");
        List<Share> stocks = null;

        try {
            stocks = investApi.getInstrumentsService()
                    .getAllShares()
                    .get().subList(0, 200);
            log.info("Getting stocks from api - {}", (stocks != null));
        } catch (Exception e) {
            log.error("Bad request to external api...");
        }

        if (stocks == null) {
            throw new CannotProceedApiRequestException("Не удалось получить список акций на рынке");
        }

        return stockMapper.toDto(stocks);
    }


}
