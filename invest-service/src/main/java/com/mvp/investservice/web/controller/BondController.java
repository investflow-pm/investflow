package com.mvp.investservice.web.controller;

import com.mvp.investservice.domain.invest_items.bonds.BondDto;
import com.mvp.investservice.service.BondService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/invest/bonds")
@RequiredArgsConstructor
public class BondController {

    private final BondService bondService;

    @GetMapping
    public List<BondDto> getBonds() {
        return bondService.getAllBonds();
    }
}
