package com.mvp.investservice.web.mapper;

import com.mvp.investservice.web.dto.stock.StockDto;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Share;

@Component
public class StockMapper {

    public StockDto toDto(Share stock) {
        StockDto stockDto = new StockDto();

        stockDto.setName(stock.getName());
        stockDto.setFigi(stock.getFigi());
        stockDto.setTicker(stock.getTicker());
        stockDto.setCountryOfRiskName(stock.getCountryOfRiskName());
        stockDto.setCurrency(stock.getCurrency());
        stockDto.setSector(stock.getSector());
        stockDto.setLots((int) stock.getLot());

        return stockDto;
    }
}
