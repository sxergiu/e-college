package com.ecampus.Ecampus.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String name;
    private String email;
    private String image;
    private String studentId;
    private String phone;
    private String address;
    private String university;
    private String bio;
    private String username;
    private Double balance = 0.0;
    private Integer rating = 0;
    private Integer numRatings = 0;
    private Wishlist wishlist;
    //private List<Listing> userWishlist = new ArrayList<>();
    // Default constructor for deserialization
    public User() {}

    // Constructor with all fields
    public User(String name, String email, String image, String studentId, String phone, String address, String university, String bio, String username, Double balance, Integer rating, Integer numRatings) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.studentId = studentId;
        this.phone = phone;
        this.address = address;
        this.university = university;
        this.bio = bio;
        this.username = username;
        this.balance = balance;
        this.rating = rating;
        this.numRatings = numRatings;
        this.phone = phone;
        this.wishlist = new Wishlist(this.studentId);
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(Integer numRatings) {
        this.numRatings = numRatings;
    }

    // Improved toString() to help with debugging
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", studentId='" + studentId + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", university='" + university + '\'' +
                ", bio='" + bio + '\'' +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                ", rating=" + rating +
                ", numRatings=" + numRatings +
                '}';
    }
}
