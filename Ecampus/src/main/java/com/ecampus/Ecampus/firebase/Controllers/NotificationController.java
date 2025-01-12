package com.ecampus.Ecampus.firebase.Controllers;

import com.ecampus.Ecampus.entities.NotificationRequest;
import com.ecampus.Ecampus.firebase.Services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Create a new notification.
     *
     * @param request The NotificationRequest object containing userId, title, and message.
     * @return The ID of the created notification or an error message.
     */
    @PostMapping("/send")
    public ResponseEntity<String> postNotification(@RequestBody NotificationRequest request) {
        try {
            // Ensure that itemId is passed in the request body
            if (request.getItemId() == null || request.getItemId().isEmpty()) {
                return ResponseEntity.status(400).body("Error: Item ID is required.");
            }

            // Call the service method to post the notification, passing the itemId as well
            String notificationId = notificationService.postNotification(request);

            return ResponseEntity.ok("Notification created with ID: " + notificationId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating notification: " + e.getMessage());
        }
    }


    /**
     * Retrieve all notifications for a specific user.
     *
     * @param userId The ID of the user whose notifications are to be retrieved.
     * @return A list of notifications or an error message.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotifications(@PathVariable String userId) {
        try {
            List<Map<String, Object>> notifications = notificationService.getNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching notifications: " + e.getMessage());
        }
    }

    @PutMapping("/{notificationId}/read")
    public String markNotificationAsRead(@PathVariable String notificationId) {
        try {
            notificationService.markNotificationAsRead(notificationId);
            return "Notification marked as read successfully.";
        } catch (ExecutionException | InterruptedException e) {
            return "Error marking notification as read: " + e.getMessage();
        }
    }
}
