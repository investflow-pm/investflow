package com.mvp.investservice.web.mappers.impl;

import com.mvp.investservice.web.dto.StockDto;
import com.mvp.investservice.web.mappers.Mappable;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Share;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockMapper implements Mappable<Share, StockDto> {
    @Override
    public Share toEntity(StockDto dto) {
        return null;
    }

    @Override
    public StockDto toDto(Share stock) {
       StockDto stockDto = new StockDto();

       stockDto.setName(stock.getName());
       stockDto.setFigi(stock.getFigi());
       stockDto.setTicker(stock.getTicker());
       stockDto.setCountryOfRiskName(stock.getCountryOfRiskName());
       stockDto.setCurrency(stock.getCurrency());
       stockDto.setSector(stock.getSector());
       stockDto.setLot((long) stock.getLot());

       return stockDto;
    }

    @Override
    public List<Share> toEntity(List<StockDto> dto) {
        return null;
    }

    @Override
    public List<StockDto> toDto(List<Share> stocks) {
        List<StockDto> stockDtoList = new ArrayList<>();
        for (Share stock : stocks) {
            stockDtoList.add(toDto(stock));
        }
        return stockDtoList;
    }
}
