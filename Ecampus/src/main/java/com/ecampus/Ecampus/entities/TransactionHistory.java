package com.ecampus.Ecampus.entities;

import java.util.List;

public class TransactionHistory {

    private String userEmail;
    private List<Listing> transactions;
    private TransactionType type;

    public TransactionHistory(TransactionType type, List<Listing> transactions, String userEmail) {
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

    public List<Listing> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Listing> transactions) {
        this.transactions = transactions;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
