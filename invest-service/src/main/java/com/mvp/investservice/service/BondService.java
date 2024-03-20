package com.mvp.investservice.service;

import com.mvp.investservice.web.dto.OrderResponse;
import com.mvp.investservice.web.dto.PurchaseDto;
import com.mvp.investservice.web.dto.bond.BondDto;

import java.util.List;

public interface BondService {
    List<BondDto> getBondsByName(String name);

    List<BondDto> getBonds(Integer page, Integer count);

    List<BondDto> getBondsBySector(String sectorName, Integer count);

    OrderResponse<BondDto> buyBond(PurchaseDto purchaseDto);
}
