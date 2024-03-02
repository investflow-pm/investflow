package com.mvp.crudmicroservice.stock.domain;

import com.mvp.crudmicroservice.portfolio.domain.UserStockPortfolio;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "stocks")
@Data
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;

    @Column(unique = true)
    private String figi;

    @Column(unique = true)
    private String name;

    private String sector;

    private String currency;

    private String countryOfRiskName;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL)
    private List<UserStockPortfolio> userPortfolios;

    @Column(name = "shares_per_lot")
    private int sharesPerLot;
}
