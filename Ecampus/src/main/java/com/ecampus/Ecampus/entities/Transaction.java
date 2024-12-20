package com.ecampus.Ecampus.entities;

import com.ecampus.Ecampus.entities.Item;
import com.ecampus.Ecampus.entities.User;

import java.util.Date;

public class Transaction {
    private String id;
    private User buyer; // Buyer information
    private User seller; // Seller information
    private Item item; // Item involved in the transaction
    private Date transactionDate; // Date of the transaction

    // Constructor
    public Transaction(String id, User buyer, User seller, Item item, Date transactionDate, String status) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.item = item;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", buyer=" + buyer.getName() +
                ", seller=" + seller.getName() +
                ", item=" + item.getName() +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
