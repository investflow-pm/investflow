package com.mvp.crudmicroservice.bond.service;

import com.mvp.crudmicroservice.bond.domain.Bond;

import java.util.List;

public interface BondService {

    Bond add(Bond bond, Long userId, Long count);

    List<Bond> getAllByUserId(Long userId);
}
