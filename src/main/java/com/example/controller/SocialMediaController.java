package com.example.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    AccountService accSer;


    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account acc){
        Account loginAttempt = accSer.tryLogin(acc.getUsername(), acc.getPassword());
        if (loginAttempt != null){
            return ResponseEntity.ok(loginAttempt);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account newAcc ){
        if (newAcc.getUsername() == null){
            return ResponseEntity.status(400).build();
        } else if (newAcc.getPassword() == null || newAcc.getPassword().length() < 4){
            return ResponseEntity.status(400).build();
        } else if (accSer.findByUsername(newAcc.getUsername()) != null){
            return ResponseEntity.status(409).build();
        } else{
            Account savedAcc = accSer.registerAccount(newAcc);
            return ResponseEntity.ok(savedAcc);
        }

    }
}
