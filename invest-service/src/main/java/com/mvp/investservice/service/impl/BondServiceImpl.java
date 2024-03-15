package com.mvp.investservice.service.impl;

import com.mvp.investservice.domain.exception.ResourceNotFoundException;
import com.mvp.investservice.service.BondService;
import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.bond.BondDto;
import com.mvp.investservice.web.mapper.BondMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Bond;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BondServiceImpl implements BondService {

    private final InvestApi investApi;
    private final BondMapper bondMapper;

    @Override
    public List<BondDto> getBondsByName(String name) {
        var bondsInfo = investApi.getInstrumentsService().findInstrumentSync(name)
                .stream().filter(b -> b.getInstrumentType().equalsIgnoreCase("bond")).toList();
        if (bondsInfo.isEmpty()) {
            throw new ResourceNotFoundException("Не удалось найти облигацию: " + name);
        }

        List<String> bondFigis = new ArrayList<>(bondsInfo.size());
        for (var bond : bondsInfo) {
            bondFigis.add(bond.getFigi());
        }

        List<Bond> bonds = new ArrayList<>();
        for (var figi : bondFigis) {
            bonds.add(investApi.getInstrumentsService().getBondByFigiSync(figi));
        }

        return bondMapper.toDto(bonds);
    }

    @Override
    public List<BondDto> getBonds(Integer page, Integer count) {
        var tradableBonds = investApi.getInstrumentsService().getTradableBondsSync().subList(page * count, count * (page - 1));

        List<BondDto> bonds = new ArrayList<>();
        for (var bond : tradableBonds) {
            bonds.add(bondMapper.toDto(bond));
        }

        return bonds;
    }

    @Override
    public List<BondDto> getBondsBySector(String sectorName) {
        return null;
    }

    @Override
    public OrderResponse<BondDto> buyBond(PurchaseDto purchaseDto) {
        return null;
    }
}
