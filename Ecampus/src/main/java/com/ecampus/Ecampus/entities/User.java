package com.ecampus.Ecampus.entities;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private String name;
    private String email;
    private String profilePicture;
    private String studentId;
    private String phone;
    private String address;
    private String university;
    private String bio;
    private String username;
    private Double balance = 0.0;
    private Integer rating;
    private Integer numRatings;
    //private List<Listing> userListings = new ArrayList<>();
    //private List<Listing> userWishlist = new ArrayList<>();


    public User()
    {}

    public User(String name, String email, String profilePicture, String studentId, String university, String bio, String username, double balance, Integer rating, Integer numRatings, String phone)
    {
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
        this.studentId = studentId;
        this.university = university;
        this.bio = bio;
        this.username = username;
        this.balance = balance;
        this.rating = rating;
        this.numRatings = numRatings;
        this.phone = phone;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getProfilePicture()
    {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture)
    {
        this.profilePicture = profilePicture;
    }

    public String getStudentId()
    {
        return studentId;
    }

    public void setStudentId(String studentId)
    {
        this.studentId = studentId;
    }

    public String getUniversity()
    {
        return university;
    }

    public void setUniversity(String university)
    {
        this.university = university;
    }

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public double getBalance()
    {
        return balance;
    }

    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    public Integer getRating()
    {
        return rating;
    }

    public void setRating(Integer rating)
    {
        this.rating = rating;
    }

    public Integer getNumRatings()
    {
        return numRatings;
    }

    public void setNumRatings(Integer numRatings)
    {
        this.numRatings = numRatings;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
