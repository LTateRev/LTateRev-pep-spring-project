package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;

import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    AccountRepository accRep;

    public Account tryLogin(String user, String pass){
        return accRep.findByUsernameAndPassword(user, pass);
    }

    public Account findByUsername(String username){
        return accRep.findByUsername(username);
    }

    public Account registerAccount( Account acc){
        return accRep.save(acc);
    }
}
