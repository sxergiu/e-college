package com.ecampus.Ecampus.entities;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class Item
{

    private String id;
    private String sellerId; // maps to user id
    private String name;
    private String description;
    private double price;
    private List<String> images;
    private boolean isSold = false;
    private ItemCondition condition;
    private ItemCategory category;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
    private Timestamp updatedAt;

    public Item()
    {
    }

    public Item(String sellerId, String name, String description, double price, ItemCategory category, List<String> images, ItemCondition condition) {
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.images = images;
        this.condition = condition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images)
    {
        this.images = images;
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

    public ItemCategory getCategory()
    {
        return this.category;
    }

    public void setCategory(String categoryString) {
        try {
            this.category = ItemCategory.valueOf(categoryString);
        } catch (IllegalArgumentException e) {
            // Handle the case where the string doesn't match any enum constant
            System.err.println("Invalid category string: " + categoryString);
            // Either throw an exception, set a default value, or log a warning
            this.category = null; // Or a default category
        }
    }

    public ItemCondition getCondition()
    {
        return condition;
    }

    public void setCondition(String conditionString) {
        if (conditionString == null || conditionString.isEmpty()) {
            this.condition = null; // Or set a default
            return;
        }
        try {
            this.condition = ItemCondition.valueOf(conditionString.toUpperCase()); // Case-insensitive
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid condition string: " + conditionString);
            this.condition = null; // Or a default condition
        }
    }


    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", images=" + images +
                ", condition='" + condition + '\'' +
                ", sold=" + isSold +
                ", createdAt=" + createdAt +
                '}';
    }
}
