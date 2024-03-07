package com.mvp.crudmicroservice.bond.repository;

import com.mvp.crudmicroservice.bond.domain.Bond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BondRepository extends JpaRepository<Bond, Long> {

    Bond findBondByTicker(String ticker);
}
