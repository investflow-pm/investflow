package com.mvp.investservice.service;

import com.mvp.investservice.domain.invest_items.bonds.BondDto;

import java.util.List;

public interface BondService {

    List<BondDto> getAllBonds();
}
