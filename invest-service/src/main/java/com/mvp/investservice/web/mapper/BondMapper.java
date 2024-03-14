package com.mvp.investservice.web.mapper;

import com.mvp.investservice.util.MoneyParser;
import com.mvp.investservice.web.dto.bond.BondDto;
import com.mvp.investservice.web.dto.bond.RiskLevel;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Bond;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class BondMapper {

    public BondDto toDto(Bond bond) {
        BondDto bondDto = new BondDto();

        bondDto.setCurrency(bond.getCurrency());
        bondDto.setFigi(bond.getFigi());
        bondDto.setLots(bond.getLot());
        bondDto.setName(bond.getName());
        bondDto.setSector(bond.getSector());
        bondDto.setCountryOfRiskName(bond.getCountryOfRiskName());
        bondDto.setTicker(bond.getTicker());
        bondDto.setMaturityDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(bond.getMaturityDate().getSeconds(), bond.getMaturityDate().getNanos()), ZoneId.systemDefault()));
        bondDto.setPlacementPrice(MoneyParser.moneyValueToBigDecimal(bond.getPlacementPrice()));
        bondDto.setRiskLevel(RiskLevel.valueOf(bond.getRiskLevel().name()));
        bondDto.setCouponQuantityPerYear(bond.getCouponQuantityPerYear());

        return bondDto;
    }
}
