package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.CannotProceedApiRequestException;
import com.mvp.investservice.domain.invest_items.bonds.BondDto;
import com.mvp.investservice.service.BondService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Bond;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BondServiceImpl implements BondService {

    private final InvestApi investApi;

    @Override
    public List<BondDto> getAllBonds() {
        log.info("Entering getAllStocks service method...");
        List<Bond> bondDtos = null;

        try {
            bondDtos = investApi.getInstrumentsService()
                    .getAllBonds()
                    .get().subList(0, 200);
            log.info("Getting stocks from api - {}", (bondDtos != null));
        } catch (Exception e) {
            log.error("Bad request to external api...");
        }

        if (bondDtos == null) {
            throw new CannotProceedApiRequestException("Не удалось получить список акций на рынке");
        }

        return null;
    }
}
