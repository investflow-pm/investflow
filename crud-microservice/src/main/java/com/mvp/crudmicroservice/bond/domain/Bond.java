package com.mvp.crudmicroservice.bond.domain;

import com.mvp.crudmicroservice.portfolio.domain.UserBondPortfolio;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "bonds")
@Data
public class Bond {

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

    private String logoName;

    // TODO: check coupon info
    //private String coupon;

    @OneToMany(mappedBy = "bond", cascade = CascadeType.ALL)
    private List<UserBondPortfolio> userPortfolios;
}
