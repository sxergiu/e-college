package com.ecampus.Ecampus.firebase.Controllers;

import com.ecampus.Ecampus.firebase.Services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
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

    @PostMapping("/create")
    public ResponseEntity<String> createChat(@RequestBody Map<String, Object> request) {
        try {
            // Extract participants from the request body
            if (!request.containsKey("participants")) {
                return ResponseEntity.badRequest().body("Participants are required.");
            }

            // Correct way to handle participants as a list
            List<String> participants = (List<String>) request.get("participants");

            // Check if participants list is empty
            if (participants == null || participants.isEmpty()) {
                return ResponseEntity.badRequest().body("At least one participant is required.");
            }

            // Check if a chat with the same participants already exists
            String existingChatId = chatService.findChatByParticipants(participants);
            if (existingChatId != null) {
                // Return OK if the chat already exists
                return ResponseEntity.ok("Chat already exists with ID: " + existingChatId);
            }

            // Create the chat if no existing chat was found
            String chatId = chatService.createChat(participants.toArray(new String[0])); // Convert to array if necessary
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

    @GetMapping("/{chatId}/messages")
    public List<Map<String, Object>> getMessages(@PathVariable String chatId) {
        try {
            return chatService.getMessagesWithUsernames(chatId); // Fetch messages with usernames
        } catch (Exception e) {
            // Handle exception properly (e.g., return an error response)
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
