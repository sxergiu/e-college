package com.ecampus.Ecampus.entities;

import com.google.cloud.Timestamp;

public class Rating
{
    private String reviewerId;
    private String revieweeId;
    private String itemId;
    private double rating;
    private String comment;
    private Timestamp timestamp;

    public Rating(){}

    public Rating(String reviewerId, String revieweeId, String itemId, double rating, String comment, Timestamp timestamp)
    {
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
        this.itemId = itemId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getReviewerId()
    {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId)
    {
        this.reviewerId = reviewerId;
    }

    public String getRevieweeId()
    {
        return revieweeId;
    }

    public void setRevieweeId(String revieweeId)
    {
        this.revieweeId = revieweeId;
    }

    public String getItemId()
    {
        return itemId;
    }

    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }

    public double getRating()
    {
        return rating;
    }

    public void setRating(double rating)
    {
        this.rating = rating;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }
}
