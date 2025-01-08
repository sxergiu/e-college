package com.ecampus.Ecampus.firebase.Services;

import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final Firestore db;

    public ChatService(FirebaseApp firebaseApp) {
        this.db = FirestoreClient.getFirestore();
    }

    // Create a new chat
    public String createChat(String[] participantsList) throws Exception {

        List<String> participants = Arrays.asList(participantsList);

        Map<String, Object> chat = new HashMap<>();
        chat.put("participants", participants);
        chat.put("lastMessage", "");
        chat.put("Time", FieldValue.serverTimestamp());

        DocumentReference chatRef = db.collection("chats").document();
        chatRef.set(chat).get();
        return chatRef.getId();
    }

    // Add a message to a chat
    public void addMessage(String chatId, String senderId, String messageContent) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("senderId", senderId);
        message.put("message", messageContent);
        message.put("Timestamp", FieldValue.serverTimestamp());

        db.collection("chats").document(chatId).collection("messages").add(message).get();
    }
}
