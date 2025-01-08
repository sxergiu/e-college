package com.ecampus.Ecampus.firebase.Services;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.HashMap;
import java.util.Map;

public class ChatService {

    private final Firestore db = FirestoreClient.getFirestore();

    // Create a new chat
    public String createChat(String[] participants) throws Exception {
        Map<String, Object> chat = new HashMap<>();
        chat.put("participants", participants);
        chat.put("lastMessage", "");
        chat.put("timestamp", FieldValue.serverTimestamp());

        DocumentReference chatRef = db.collection("chats").document();
        chatRef.set(chat);
        return chatRef.getId();
    }

    // Add a message to a chat
    public void addMessage(String chatId, String senderId, String messageContent) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("senderId", senderId);
        message.put("message", messageContent);
        message.put("timestamp", FieldValue.serverTimestamp());

        db.collection("chats").document(chatId).collection("messages").add(message);
    }
}
