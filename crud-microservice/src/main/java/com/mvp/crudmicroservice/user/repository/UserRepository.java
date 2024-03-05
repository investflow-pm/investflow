package com.mvp.crudmicroservice.user.repository;

import com.mvp.crudmicroservice.user.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}