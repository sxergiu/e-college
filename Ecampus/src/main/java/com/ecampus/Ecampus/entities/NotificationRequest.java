package com.ecampus.Ecampus.entities;

public class NotificationRequest {
    private String userId;
    private String title;
    private String message;

    public NotificationRequest() {
    }

    public NotificationRequest(String userId, String title, String message) {
        this.userId = userId;
        this.title = title;
        this.message = message;
    }

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
}
