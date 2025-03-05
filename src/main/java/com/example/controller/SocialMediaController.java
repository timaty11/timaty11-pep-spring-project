package com.example.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

@Controller
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // Process new User registrations. Endpoint: POST localhost:8080/register
    // If successful the response body will contain a JSON of the Account, including its accountId.
    // If the registration is not successful due to a duplicate username, the response status will be 409. (Conflict)
    // If the registration is not successful for some other reason, the response status will be 400. (Client error)
    @PostMapping(value = "/register")
    public ResponseEntity<Account> registerHandler(@RequestBody Account account) {
        
        // Basic Checkups
        if (account == null || 
            account.getUsername().isBlank() || 
            account.getUsername().isEmpty() ||
            account.getPassword().length() < 4) {
                return ResponseEntity.status(400).body(null);
        }

        // Check if account with such username already exists
        Account accountCheck = accountService.getAccountByUsername(account.getUsername());
        System.err.println("Account check: " + accountCheck);
        if (accountCheck != null) {
            return ResponseEntity.status(409).body(null);
        }        

        // If everything checks up create new account
        Account createdAccount = accountService.createAccount(account);
        System.err.println("Created acc: " + createdAccount);
        return ResponseEntity.status(200).body(createdAccount);
    }


    // Process User logins. Endpoint: POST localhost:8080/login
    // If login was successful the response body will contain a JSON of the account.
    // If not successful the response status will be 401. (Unauthorized)
    @PostMapping(value = "/login")
    public ResponseEntity<Account> loginHandler(@RequestBody Account account) {
        Account checkedAccount = accountService.checkPasswordMatch(account);

        if (checkedAccount != null) {
            return ResponseEntity.status(200).body(checkedAccount);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }


    // Process creation of new messages. Endpoint: POST localhost:8080/messages
    // If message was created successfully the response body will contain a JSON of the message.
    // If not successful, the response status will be 400. (Client error)
    @PostMapping(value = "/messages")
    public ResponseEntity<Message> addMessageHandler(@RequestBody Message enteredMessage) {
        
        // Basic checkups
        if (enteredMessage == null || 
            enteredMessage.getMessageText().isBlank() || 
            enteredMessage.getMessageText().isEmpty() ||
            enteredMessage.getMessageText().length() > 255) {
                return ResponseEntity.status(400).body(null);
        }

        // Check if account with such postedBy ID exists
        Account accountCheck = accountService.getAccountByID(enteredMessage.getPostedBy());
        if (accountCheck == null) {
            return ResponseEntity.status(400).body(null);
        }        

        // If everything checks up create new message
        Message createdMessage = messageService.createMessage(enteredMessage);
        return ResponseEntity.status(200).body(createdMessage);
    }


    // Process retreival of all messages. Endpoint: GET localhost:8080/messages
    // The response body will contain a JSON representation of a list containing all messages retrieved from the database.
    // The response status should always be 200.
    @GetMapping(value = "/messages")
    public ResponseEntity<List<Message>> getAllMessagesHandler() {
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }


    // Process retreival of a message by its ID. Endpoint: GET localhost:8080/messages/{messageId}
    // If successful response body will contain a JSON representation of the message.
    // If not successful due to message not found, the response will be empty.
    // The response status should always be 200.
    @GetMapping(value = "/messages/{messageId}")
    public ResponseEntity<Message> getMessageByIDHandler(@PathVariable int messageId) {
        return ResponseEntity.status(200).body(messageService.getMessageByID(messageId));
    }


    // Process deletion of a message by its ID. Endpoint: DELETE localhost:8080/messages/{messageId}
    // If successful the response body should contain the number of rows updated.
    // If not successful due to message not found, the response body should be empty.
    // The response status should always be 200.
    @DeleteMapping(value = "/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageByIDHandler(@PathVariable int messageId) {
        return ResponseEntity.status(200).body(messageService.deleteMessage(messageId) == 1 ? 1 : null);
    }


    // Process update of a message by its ID. Endpoint: PATCH localhost:8080/messages/{messageId}
    // If successful the response body should contain the number of rows updated.
    // If not successful due to any reason, the response status should be 400. (Client error)
    @PatchMapping(value = "/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageByIDHandler(@PathVariable int messageId, @RequestBody Message enteredMessage) {
        
        // Basic checkups
        if (enteredMessage == null || 
            enteredMessage.getMessageText().isBlank() || 
            enteredMessage.getMessageText().isEmpty() ||
            enteredMessage.getMessageText().length() > 255) {
                return ResponseEntity.status(400).body(null);
        }

        // Check if such message already exists
        Message messageCheck = messageService.getMessageByID(messageId);
        if (messageCheck == null) {
            return ResponseEntity.status(400).body(null);
        }        

        // If everything checks up update message
        messageService.createMessage(enteredMessage);
        return ResponseEntity.status(200).body(1);
    }


    // Process retreival of all messages by author ID. Endpoint: GET localhost:8080/accounts/{accountId}/messages
    // If successful the response body should contain a JSON representation of a list
    // containing all messages posted by a particular user
    // If not successful the list will be empty if there are no messages.
    // The response status should always be 200.
    @GetMapping(value = "/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesPostedByHandler(@PathVariable int accountId) {
        return ResponseEntity.status(200).body(messageService.getMessagePostedByID(accountId));
    }

}
