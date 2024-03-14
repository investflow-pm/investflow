package com.mvp.investservice.web.mapper;

import com.mvp.investservice.web.dto.CurrencyDto;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Currency;

@Component
public class CurrencyMapper {

    public CurrencyDto toDto(Currency currency) {
        CurrencyDto currencyDto = new CurrencyDto();

        currencyDto.setCurrency(currency.getCurrency());
        currencyDto.setFigi(currency.getFigi());
        currencyDto.setName(currency.getName());
        currencyDto.setTicker(currency.getTicker());
        currencyDto.setCountryOfRiskName(currency.getCountryOfRiskName());

        return currencyDto;
    }
}
