package com.ecampus.Ecampus.entities;

import java.sql.Timestamp;
import java.util.List;

public class Listing {

    private String listingId;
    private String sellerEmail;
    private String listingTitle;
    private String listingDescription;
    private double listingPrice;
    private List<String> Images;
    private boolean isSold = false;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
    private Timestamp updatedAt;

    public Listing(List<String> images, double listingPrice, String listingDescription, String listingTitle, String sellerEmail, String listingId, Timestamp updatedAt) {
        Images = images;
        this.listingPrice = listingPrice;
        this.listingDescription = listingDescription;
        this.listingTitle = listingTitle;
        this.sellerEmail = sellerEmail;
        this.listingId = listingId;
        this.updatedAt = updatedAt;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public String getListingDescription() {
        return listingDescription;
    }

    public void setListingDescription(String listingDescription) {
        this.listingDescription = listingDescription;
    }

    public double getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(double listingPrice) {
        this.listingPrice = listingPrice;
    }

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> images) {
        Images = images;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
