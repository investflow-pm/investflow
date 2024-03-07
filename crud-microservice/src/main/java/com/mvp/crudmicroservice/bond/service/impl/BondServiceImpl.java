package com.mvp.crudmicroservice.bond.service.impl;

import com.mvp.crudmicroservice.AssetService;
import com.mvp.crudmicroservice.bond.domain.Bond;
import com.mvp.crudmicroservice.bond.repository.BondRepository;
import com.mvp.crudmicroservice.portfolio.domain.UserBondPortfolio;
import com.mvp.crudmicroservice.portfolio.repository.UserBondPortfolioRepository;
import com.mvp.crudmicroservice.user.domain.exception.ResourceNotFoundException;
import com.mvp.crudmicroservice.user.domain.user.User;
import com.mvp.crudmicroservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BondServiceImpl implements AssetService<Bond> {

    private final UserRepository userRepository;

    private final BondRepository bondRepository;

    private final UserBondPortfolioRepository userBondPortfolioRepository;

    // TODO: check (may be quantity?)
    @Override
    public Bond buyAsset(Bond bond, Long userId, Long lots) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с там id не найден"));

        bond = getBondFromDb(bond);

        var userBondPortfolio
                = userBondPortfolioRepository.findByUserAndBond(user, bond);

        userBondPortfolio = updateUserBondPortfolio(bond, lots, userBondPortfolio, user);
        userBondPortfolioRepository.save(userBondPortfolio);

        return bond;
    }

    // TODO: change lot to quantity?
    private static UserBondPortfolio updateUserBondPortfolio(Bond bond, Long lots,
                                                              UserBondPortfolio userBondPortfolio, User user) {
        if (userBondPortfolio == null) {
            userBondPortfolio = new UserBondPortfolio();
            userBondPortfolio.setBond(bond);
            userBondPortfolio.setUser(user);
            userBondPortfolio.setQuantity(lots);
        } else {
            userBondPortfolio.setQuantity(userBondPortfolio.getQuantity() + lots);
        }
        return userBondPortfolio;
    }

    private Bond getBondFromDb(Bond bond) {
        Bond dbBond = bondRepository.findBondByTicker(bond.getTicker());

        if (dbBond == null) {
            bond = bondRepository.save(bond);
            return bond;
        } else {
            return dbBond;
        }
    }
}
