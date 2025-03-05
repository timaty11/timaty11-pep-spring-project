package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Retrieve all messages from DB
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    

    // Retrieve message by its id. Returns message obj if record with such message_id
    // exists, null otherwise
    public Message getMessageByID(int message_id) {
        Optional<Message> optionalMessage = messageRepository.findById(message_id);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        } else {
            return null;
        }
    }

    
    // Retrieve message by its postedBy ID. Returns messages obj list if records with such
    // postedBy ID exists, null otherwise
    public List<Message> getMessagePostedByID(int postedBy) {
        Optional<List<Message>> optionalMessage = messageRepository.findByPostedBy(postedBy);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        } else {
            return null;
        }
    }

    
    // Add message to a DB
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }
    

    // Delete message from a DB
    public int deleteMessage(int messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            messageRepository.deleteById(messageId);
            return 1;
        } else {
            return 0;
        }
    }    
}
