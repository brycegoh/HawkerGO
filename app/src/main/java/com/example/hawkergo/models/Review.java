package com.example.hawkergo.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Review extends BaseDbFields {
    private String comment, name, hawkerStall;
    private Double stars;
    private Date dateReviewed;
    private String imageUrl;

    public Review(){};


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHawkerStall() {
        return hawkerStall;
    }

    public void setHawkerStall(String hawkerStall) {
        this.hawkerStall = hawkerStall;
    }

    public Double getStars() {
        return stars;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    public Date getDateReviewed() {
        return dateReviewed;
    }

    public void setDateReviewed(Date dateReviewed) {
        this.dateReviewed = dateReviewed;
    }

    public Review(String name, String comment, Double stars, Date dateReviewed, String hawkerStall, String imageUrl){
        /**
         * New review document:
         *
         * @param name Name of the person giving the review
         * @param comment Main text of feedback
         * @param stars Rating out 5
         * @param dateReviewed Date of review
         * @param hawkerStall ID of hawker stall
         */

        this.name = name;
        this.comment = comment;
        this.stars = stars;
        this.dateReviewed = dateReviewed;
        this.hawkerStall = hawkerStall;
        this.imageUrl = imageUrl;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        map.put("comment", this.comment);
        map.put("stars", this.stars);
        map.put("dateReviewed", this.dateReviewed);
        map.put("hawkerStall", this.hawkerStall);
        return map;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    @Override
//    public String toString(){
//        return "name: " + this.name;
//    }
}
