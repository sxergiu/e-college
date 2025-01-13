package com.ecampus.Ecampus.entities;

import java.util.Date;

public class Transaction {
    private String itemId;
    private String buyerId;
    private String sellerId;
    private Date timestamp;

    // Getters and Setters
    public Transaction()
    {

    }

    public Transaction(String itemId, String buyerId, String sellerId, Date timestamp)
    {
        this.itemId = itemId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.timestamp = timestamp;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String toString()
    {
        return "Transaction: " + itemId + ", " + buyerId + ", " + sellerId + ", " + timestamp;
    }
}
