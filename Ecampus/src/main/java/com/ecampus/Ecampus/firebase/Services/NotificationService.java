package com.ecampus.Ecampus.firebase.Services;

import com.ecampus.Ecampus.entities.NotificationRequest;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class NotificationService {

    private final Firestore db;

    public NotificationService() {
        this.db = FirestoreClient.getFirestore();
    }

    public void markNotificationAsRead(String notificationId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("notifications").document(notificationId);
        ApiFuture<WriteResult> future = docRef.update("isRead", true);
        future.get(); // Wait for the update to complete
    }

    // Create a new notification
    public String postNotification(NotificationRequest request) throws ExecutionException, InterruptedException {
        Map<String, Object> notification = new HashMap<>();
        notification.put("userId", request.getUserId());
        notification.put("title", request.getTitle());
        notification.put("message", request.getMessage());
        notification.put("isRead", false);
        notification.put("timestamp", FieldValue.serverTimestamp());
        notification.put("itemId", request.getItemId());  // Add itemId to track the product related to the notification

        DocumentReference notificationRef = db.collection("notifications").document();
        notificationRef.set(notification).get();
        return notificationRef.getId();
    }


    // Retrieve notifications for a specific user
    public List<Map<String, Object>> getNotifications(String userId) throws ExecutionException, InterruptedException {
        List<Map<String, Object>> notifications = new ArrayList<>();

        // Query notifications where userId is matched
        ApiFuture<QuerySnapshot> future = db.collection("notifications")
                .whereEqualTo("userId", userId)
                .get();

        // Process each notification and filter the isRead status in memory
        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            Map<String, Object> notification = document.getData();
            if (!(Boolean) notification.get("isRead")) {  // Check if it's unread
                notification.put("id", document.getId()); // Add document ID for reference
                notifications.add(notification);
            }
        }

        // Sort notifications by timestamp in descending order using a comparator
        notifications.sort((n1, n2) -> ((Timestamp) n2.get("timestamp")).compareTo((Timestamp) n1.get("timestamp")));

        return notifications;
    }

}
