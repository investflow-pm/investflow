package com.mvp.crudmicroservice.portfolio.repository;

import com.mvp.crudmicroservice.bond.domain.Bond;
import com.mvp.crudmicroservice.portfolio.domain.UserBondPortfolio;
import com.mvp.crudmicroservice.user.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBondPortfolioRepository extends JpaRepository<UserBondPortfolio, Long> {

    UserBondPortfolio findByUserAndBond(User user, Bond bond);
}
