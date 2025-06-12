package com.example.controller;

import java.lang.annotation.Repeatable;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId){
        Message mess = messSer.messageById(messageId).orElse(null);
        return ResponseEntity.ok(mess);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessageByAccount(@PathVariable int accountId){
        List<Message> mess = messSer.messageByAccountId(accountId);
        return ResponseEntity.ok(mess);
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

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageTxt(@PathVariable int messageId, @RequestBody Message updateMess){
        String newMess = updateMess.getMessageText();
        if(newMess == null || newMess.isEmpty() || newMess.length() > 255){
            return ResponseEntity.status(400).build();
        }
        int checkUpdate = messSer.updateMessage(messageId, newMess);

        if (checkUpdate == 1){
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable int messageId){
        int deletedMess = messSer.deleteMessageById(messageId);

        if (deletedMess == 1) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok().build();
        }
    }

}