package com.mvp.crudmicroservice.user.domain.user;

import com.mvp.crudmicroservice.portfolio.domain.UserStockPortfolio;
import com.mvp.crudmicroservice.stock.domain.Stock;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    private String password;

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles")
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserStockPortfolio> stockPortfolio;

    private String investAccountId;
}
