package com.mvp.crudmicroservice.stock.service.impl;

import com.mvp.crudmicroservice.AssetService;
import com.mvp.crudmicroservice.portfolio.domain.UserStockPortfolio;
import com.mvp.crudmicroservice.portfolio.repository.UserStockPortfolioRepository;
import com.mvp.crudmicroservice.stock.domain.Stock;
import com.mvp.crudmicroservice.stock.repository.StockRepository;
import com.mvp.crudmicroservice.user.domain.exception.ResourceNotFoundException;
import com.mvp.crudmicroservice.user.domain.user.User;
import com.mvp.crudmicroservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements AssetService<Stock> {

    private final UserRepository userRepository;

    private final StockRepository stockRepository;

    private final UserStockPortfolioRepository userStockPortfolioRepository;

    @Override
    public Stock buyAsset(Stock stock, Long userId, Long lots) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с там id не найден"));

        stock = getStockFromDb(stock);

        UserStockPortfolio userStockPortfolio
                = userStockPortfolioRepository.findByUserAndStock(user, stock);

        userStockPortfolio = updateUserStockPortfolio(stock, lots, userStockPortfolio, user);
        userStockPortfolioRepository.save(userStockPortfolio);

        return stock;
    }

    private static UserStockPortfolio updateUserStockPortfolio(Stock stock, Long lots, UserStockPortfolio userStockPortfolio, User user) {
        if (userStockPortfolio == null) {
            userStockPortfolio = new UserStockPortfolio();
            userStockPortfolio.setStock(stock);
            userStockPortfolio.setUser(user);
            userStockPortfolio.setLots(lots);
            userStockPortfolio.setQuantity(lots * stock.getSharesPerLot());
        } else {
            userStockPortfolio.setLots(userStockPortfolio.getLots() + lots);
            userStockPortfolio.setQuantity(userStockPortfolio.getLots() * userStockPortfolio.getStock().getSharesPerLot());
        }
        return userStockPortfolio;
    }

    private Stock getStockFromDb(Stock stock) {
        Stock dbStock = stockRepository.findStockByTicker(stock.getTicker());

        if (dbStock == null) {
            stock = stockRepository.save(stock);
            return stock;
        } else {
            return dbStock;
        }
    }
}
