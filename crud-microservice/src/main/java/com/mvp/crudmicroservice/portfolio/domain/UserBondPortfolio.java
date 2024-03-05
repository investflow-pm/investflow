package com.mvp.crudmicroservice.portfolio.domain;

import com.mvp.crudmicroservice.bond.domain.Bond;
import com.mvp.crudmicroservice.user.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_bond_portfolio")
@Data
public class UserBondPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bond_id")
    private Bond bond;

    private Long quantity;
}
