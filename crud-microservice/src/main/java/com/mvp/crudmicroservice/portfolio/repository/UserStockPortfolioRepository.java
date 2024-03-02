package com.mvp.crudmicroservice.portfolio.repository;

import com.mvp.crudmicroservice.portfolio.domain.UserStockPortfolio;
import com.mvp.crudmicroservice.stock.domain.Stock;
import com.mvp.crudmicroservice.user.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStockPortfolioRepository extends JpaRepository<UserStockPortfolio, Long> {

    UserStockPortfolio findByUserAndStock(User user, Stock stock);
}
