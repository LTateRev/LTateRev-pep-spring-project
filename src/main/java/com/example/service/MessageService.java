package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository messRep;

    public List<Message> getAllMessages(){
        return messRep.findAll();
    }

    public Optional <Message> messageById(int id){
        return messRep.findById(id);
    }

    public List<Message> messageByAccountId(int accountId){
        return messRep.findByPostedBy(accountId);
    }

    public Message createMessage(Message message){
        return messRep.save(message);
    }

    public int updateMessage(int messageId, String newMessage){
        Optional<Message> checkMess = messRep.findById(messageId);
        if (checkMess.isPresent()){
            Message mess = checkMess.get();
            mess.setMessageText(newMessage);
            messRep.save(mess);
            return 1;
        }
        return 0;
    }

    public int deleteMessageById(int messageId){
        return messRep.deleteByMessageId(messageId);
    }
}
