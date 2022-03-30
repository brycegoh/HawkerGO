package com.example.hawkergo.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HawkerStall extends BaseDbFields {
    // TODO: Need to add in imageUrl for the hawker stall
    // TODO: Do we want a field to track numReviews only? Thinking of storage space concerns
    public String address , name, imageUrl, hawkerCentreID;
    public OpeningHours openingHours;
    public List<Review> reviews;
    public List<String> reviewsIds;
    public List<String> popularItems;
    public List<String> tags;

    public HawkerStall(){};

    public HawkerStall(String id, String address, String name, HashMap<String,String> openingHours, String hawkerCentre, String imageUrl, List<String> reviewsIds){
        this.id = id;
        this.address = address;
        this.name = name;
        this.imageUrl = imageUrl;
        this.reviewsIds = reviewsIds;
    }

    public HawkerStall(String address, String name, OpeningHours openingHours, String imageUrl, List<String> popularItems, List<String> tags, String hawkerCentreID){

        this.address = address;
        this.name = name;
        this.imageUrl =imageUrl;
        this.openingHours = openingHours;
        this.imageUrl = imageUrl;
        this.popularItems = popularItems;
        this.tags = tags;
        this.hawkerCentreID = hawkerCentreID;
    }

    public void attachReviews(List<Review> reviews){
        this.reviews = reviews;
    }


    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("address", this.address);
        map.put("openingHours", this.openingHours);
        map.put("name", this.name);
        map.put("reviews", this.reviews);
        return map;
    }
}