package com.mvp.investservice.web.mapper;

import com.mvp.investservice.util.MoneyParser;
import com.mvp.investservice.web.dto.bond.BondDto;
import com.mvp.investservice.web.dto.bond.RiskLevel;
import com.mvp.investservice.web.dto.stock.StockDto;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Bond;
import ru.tinkoff.piapi.contract.v1.Share;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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

    public List<StockDto> toDto(List<Share> stocks) {
        List<StockDto> stockDtoList = new ArrayList<>(stocks.size());

        for (var stock : stocks) {
            var stockDto = new StockDto();

            stockDto.setName(stock.getName());
            stockDto.setFigi(stock.getFigi());
            stockDto.setTicker(stock.getTicker());
            stockDto.setCountryOfRiskName(stock.getCountryOfRiskName());
            stockDto.setCurrency(stock.getCurrency());
            stockDto.setSector(stock.getSector());
            stockDto.setLots((int) stock.getLot());

            stockDtoList.add(stockDto);
        }

        return stockDtoList;
    }
}
