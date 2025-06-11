package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    Account findByUsername(String user);

    Account findByUsernameAndPassword(String user, String pass);
}
