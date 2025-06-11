package com.example.controller;

import java.lang.annotation.Repeatable;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

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

    @Autowired
    AccountRepository accRep;


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


    @Autowired
    MessageService messSer;

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> allMessages = messSer.getAllMessages();
        return ResponseEntity.ok(allMessages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Optional<Message>> getMessageById(@PathVariable int messageId){
        Optional<Message> mess = messSer.messageById(messageId);
        if (mess == null){
            return ResponseEntity.ok(null);
        } else{
            return ResponseEntity.ok(mess);
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createNewMessage(@RequestBody Message message){
        if (message.getMessageText() == null|| message.getMessageText().isEmpty()){
            return ResponseEntity.status(400).build();
        } else if (message.getMessageText().length() > 255){
            return ResponseEntity.status(400).build();
        } else if(!accRep.existsById(message.getPostedBy())) {
            return ResponseEntity.status(400).build();
        }else{
            Message createdMessage = messSer.createMessage(message);
            return ResponseEntity.ok(createdMessage);
        }
    }
}