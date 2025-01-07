package com.ecampus.Ecampus.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Wishlist {
    private String userId; // Unique identifier for the user
    private List<String> productIds = new ArrayList<>(); // List of product IDs in the wishlist

    public Wishlist(){

    }
    // Constructor
    public Wishlist(String userId) {
        this.userId = userId;
        this.productIds = new ArrayList<>(); // Initialize an empty wishlist
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    // Add a product to the wishlist
    public boolean addProduct(String productId) {
        if (!productIds.contains(productId)) {
            productIds.add(productId);
            return true; // Successfully added
        }
        return false; // Product already exists
    }

    // Remove a product from the wishlist
    public boolean removeProduct(String productId) {
        return productIds.remove(productId); // Returns true if successfully removed
    }

    // Check if a product is in the wishlist
    public boolean containsProduct(String productId) {
        return productIds.contains(productId);
    }

    // Equals and hashCode (based on userId, as wishlist is unique per user)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wishlist wishlist = (Wishlist) o;
        return Objects.equals(userId, wishlist.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    // toString (for debugging purposes)
    @Override
    public String toString() {
        return "Wishlist{" +
                "userId=" + userId +
                ", productIds=" + productIds +
                '}';
    }
}
