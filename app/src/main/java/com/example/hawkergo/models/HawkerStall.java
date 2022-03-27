package com.example.hawkergo.models;

import java.util.HashMap;
import java.util.List;

public class HawkerStall extends BaseDbFields {
    // TODO: Need to add in imageUrl for the hawker stall
    // TODO: Do we want a field to track numReviews only? Thinking of storage space concerns
    public String id, address, hawkerCentre, name, imageUrl;
    public HashMap<String, String> openingHours;
    public List<Review> reviews;
    public List<String> reviewsIds;

    public HawkerStall(){};

    public HawkerStall(String id, String address, String name, HashMap<String,String> openingHours, String hawkerCentre, String imageUrl, List<String> reviewsIds){

        this.id = id;
        this.address = address;
        this.name = name;
        this.openingHours = openingHours;
        this.hawkerCentre = hawkerCentre;
        this.imageUrl = imageUrl;
        this.reviewsIds = reviewsIds;
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
        map.put("hawkerCentre", this.hawkerCentre);
        return map;
    }
}
