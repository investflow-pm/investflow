package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.ResourceNotFoundException;
import com.mvp.investservice.service.StockService;
import com.mvp.investservice.web.dto.StockDto;
import com.mvp.investservice.web.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final InvestApi investApi;

    private final StockMapper stockMapper;

    @Override
    public StockDto getStockByName(String name) {

        List<Share> shares = investApi.getInstrumentsService()
                .getAllSharesSync();

        Share share = shares.stream()
                .filter(e -> e.getName().contains(name))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("There is no Share with name " + name));

        return stockMapper.toDto(share);
    }
}
