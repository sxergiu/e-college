package com.ecampus.Ecampus.entities;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private String email;
    private String password;
    private String username;
    private double balance;
    private List<Listing> userListings = new ArrayList<>();
    private List<Listing> userWishlist = new ArrayList<>();

    public User()
    {}

    public User(String email, String password, String username,double balance){
        this.email = email;
        this.password = password;
        this.username = username;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
