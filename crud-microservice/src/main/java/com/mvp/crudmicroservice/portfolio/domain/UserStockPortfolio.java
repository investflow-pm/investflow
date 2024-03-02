package com.mvp.crudmicroservice.portfolio.domain;

import com.mvp.crudmicroservice.stock.domain.Stock;
import com.mvp.crudmicroservice.user.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_stock_portfolio")
@Data
public class UserStockPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private Long quantity;

    private Long lots;
}
