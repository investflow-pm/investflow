package com.mvp.crudmicroservice.stock.repository;

import com.mvp.crudmicroservice.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock findStockByTicker(String ticker);
}
