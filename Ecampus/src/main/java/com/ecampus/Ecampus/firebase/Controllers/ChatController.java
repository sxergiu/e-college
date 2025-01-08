package com.ecampus.Ecampus.firebase.Controllers;

import com.ecampus.Ecampus.firebase.Services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Endpoint to create a new chat
    @PostMapping("/create")
    public ResponseEntity<String> createChat(@RequestBody Map<String, Object> request) {
        try {
            // Extract participants from the request body
            if (!request.containsKey("participants")) {
                return ResponseEntity.badRequest().body("Participants are required.");
            }
            String[] participants = ((String) request.get("participants")).split(",");

            // Create the chat
            String chatId = chatService.createChat(participants);
            return ResponseEntity.ok("Chat created with ID: " + chatId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error creating chat: " + e.getMessage());
        }
    }

    // Endpoint to add a message to a chat
    @PostMapping("/{chatId}/messages")
    public ResponseEntity<String> addMessage(
            @PathVariable String chatId,
            @RequestBody Map<String, Object> request) {
        try {
            // Extract senderId and message from the request body
            if (!request.containsKey("senderId") || !request.containsKey("message")) {
                return ResponseEntity.badRequest().body("SenderId and message are required.");
            }

            String senderId = (String) request.get("senderId");
            String messageContent = (String) request.get("message");

            // Add the message
            chatService.addMessage(chatId, senderId, messageContent);
            return ResponseEntity.ok("Message added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error adding message: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getUserChats(@PathVariable String userId) {
        try {
            return chatService.getUserChats(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching chats", e);
        }
    }


}
