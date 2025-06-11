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

    public Message createMessage(Message message){
        return messRep.save(message);
    }
}
