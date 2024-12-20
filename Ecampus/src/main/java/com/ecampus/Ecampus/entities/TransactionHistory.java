package com.ecampus.Ecampus.entities;

import java.util.List;

public class TransactionHistory {

    private String userEmail;
    private List<Item> transactions;
    private TransactionType type;

    public TransactionHistory(TransactionType type, List<Item> transactions, String userEmail) {
        this.type = type;
        this.transactions = transactions;
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<Item> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Item> transactions) {
        this.transactions = transactions;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
