package com.ecampus.Ecampus.entities;


public class NotificationRequest {
    private String userId;
    private String title;
    private String message;
    private String itemId; // Add itemId to track the specific item

    // Constructor
    public NotificationRequest(String userId, String title, String message, String itemId) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.itemId = itemId;
    }
    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
