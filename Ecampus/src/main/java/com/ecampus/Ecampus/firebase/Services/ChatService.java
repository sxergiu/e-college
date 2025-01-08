package com.ecampus.Ecampus.firebase.Services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Map<String, Object>> getUserChats(String userId) throws Exception {
        List<Map<String, Object>> userChats = new ArrayList<>();

        // Query Firestore for chats where participants array contains the userId
        ApiFuture<QuerySnapshot> future = db.collection("chats")
                .whereArrayContains("participants", userId)
                .get();

        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            Map<String, Object> chatData = document.getData();
            chatData.put("chatId", document.getId()); // Include the chat ID
            userChats.add(chatData);
        }
        return userChats;
    }

}
