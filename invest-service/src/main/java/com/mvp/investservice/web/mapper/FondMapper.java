package com.mvp.investservice.web.mapper;

import com.mvp.investservice.web.dto.fond.FondDto;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Share;

import java.util.ArrayList;
import java.util.List;
@Component
public class FondMapper {
    public FondDto toDto(Share fond) {
        FondDto fondDto = new FondDto();

        fondDto.setName(fond.getName());
        fondDto.setFigi(fond.getFigi());
        fondDto.setTicker(fond.getTicker());
        fondDto.setCountryOfRiskName(fond.getCountryOfRiskName());
        fondDto.setCurrency(fond.getCurrency());
        fondDto.setSector(fond.getSector());
        fondDto.setLots((int) fond.getLot());

        return fondDto;
    }

    public List<FondDto> toDto(List<Share> fonds) {
        List<FondDto> fondDtoList = new ArrayList<>(fonds.size());

        for (var fond : fonds) {
            var fondDto = new FondDto();

            fondDto.setName(fond.getName());
            fondDto.setFigi(fond.getFigi());
            fondDto.setTicker(fond.getTicker());
            fondDto.setCountryOfRiskName(fond.getCountryOfRiskName());
            fondDto.setCurrency(fond.getCurrency());
            fondDto.setSector(fond.getSector());
            fondDto.setLots((int) fond.getLot());

            fondDtoList.add(fondDto);
        }

        return fondDtoList;
    }
}
