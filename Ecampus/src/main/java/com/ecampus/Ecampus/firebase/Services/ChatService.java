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
        // Create the new message
        Map<String, Object> message = new HashMap<>();
        message.put("senderId", senderId);
        message.put("message", messageContent);
        message.put("Timestamp", FieldValue.serverTimestamp());

        // Add the message to the messages subcollection
        db.collection("chats").document(chatId).collection("messages").add(message).get();

        // Update the lastMessage field in the chat document
        Map<String, Object> chatUpdate = new HashMap<>();
        chatUpdate.put("lastMessage", messageContent);
        db.collection("chats").document(chatId).update(chatUpdate);
    }

    public List<Map<String, Object>> getUserChats(String userId) throws Exception {
        List<Map<String, Object>> userChats = new ArrayList<>();

        // Query Firestore for chats where participants array contains the userId
        ApiFuture<QuerySnapshot> future = db.collection("chats")
                .whereArrayContains("participants", userId)
                .get();

        // Fetch user details for all participants except the logged-in user
        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            Map<String, Object> chatData = document.getData();
            List<String> participants = (List<String>) chatData.get("participants");

            // Remove the current userId from the participants list
            participants.remove(userId);

            // Fetch usernames for the other participants
            List<String> participantUsernames = new ArrayList<>();
            for (String participantId : participants) {
                // Fetch the user's details from the "users" collection
                ApiFuture<DocumentSnapshot> userFuture = db.collection("users")
                        .document(participantId)
                        .get();

                DocumentSnapshot userDoc = userFuture.get();
                if (userDoc.exists()) {
                    String username = userDoc.getString("username");
                    participantUsernames.add(username); // Add username to the list
                }
            }

            // Add the usernames to the chat data
            chatData.put("participantsUsernames", participantUsernames);
            chatData.put("chatId", document.getId()); // Include the chat ID
            userChats.add(chatData);
        }

        return userChats;
    }


    public List<Map<String, Object>> getMessages(String chatId) throws Exception {
        List<Map<String, Object>> messages = new ArrayList<>();

        // Query Firestore for messages in the subcollection "messages", ordered by Timestamp
        ApiFuture<QuerySnapshot> future = db.collection("chats")
                .document(chatId) // Specific chat document
                .collection("messages") // Subcollection of messages
                .orderBy("Timestamp", Query.Direction.ASCENDING) // Order messages by timestamp
                .get();

        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            Map<String, Object> message = document.getData();
            messages.add(message);
        }

        return messages;
    }

    public List<Map<String, Object>> getMessagesWithUsernames(String chatId) throws Exception {
        List<Map<String, Object>> messages = getMessages(chatId); // Retrieve the messages
        for (Map<String, Object> message : messages) {
            String senderId = (String) message.get("senderId");
            Map<String, Object> userDetails = getUserDetails(senderId); // Retrieve user details using the sender ID
            if (userDetails != null) {
                message.put("username", userDetails.get("username")); // Add username to the message
            }
        }
        return messages;
    }

    public Map<String, Object> getUserDetails(String userId) throws Exception {
        DocumentReference userRef = db.collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = userRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.getData(); // Return user data (e.g., username)
        } else {
            return null; // User not found
        }
    }


}
