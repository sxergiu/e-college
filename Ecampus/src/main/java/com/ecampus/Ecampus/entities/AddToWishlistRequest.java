package com.ecampus.Ecampus.entities;

public class AddToWishlistRequest {
    private Long itemId;
    private String userId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

