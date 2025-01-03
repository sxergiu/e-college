package com.ecampus.Ecampus.entities;

public class ProfileUpdateRequest {

    String name;
    String university;
    String phone;
    String address;

    public ProfileUpdateRequest(String name, String university, String phone, String address) {
        this.name = name;
        this.university = university;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public void setPhoneNumber(String phoneNumber) {
        phone = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
