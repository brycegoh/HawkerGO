package com.example.hawkergo.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Review extends BaseDbFields {
    public String id, comment, name, hawkerStall;
    public Double stars;
    public Date dateReviewed;

    public Review(){};

    /**
     * New review document
     *
     * @param id ID of the review
     * @param name Name of the person giving the review
     * @param comment Main text of feedback
     * @param stars Rating out 5
     * @param dateReviewed Date of review
     * @param hawkerStall ID of hawker stall
     */
    public Review(String id, String name, String comment, Double stars, Date dateReviewed, String hawkerStall){

        this.id = id;
        this.name = name;
        this.comment = comment;
        this.stars = stars;
        this.dateReviewed = dateReviewed;
        this.hawkerStall = hawkerStall;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", this.name);
        map.put("comment", this.comment);
        map.put("stars", this.stars);
        map.put("dateReviewed", this.dateReviewed);
        map.put("hawkerStall", this.hawkerStall);
        return map;
    }

//    @Override
//    public String toString(){
//        return "name: " + this.name;
//    }
}
