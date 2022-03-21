package com.example.hawkergo.models;

import java.util.HashMap;
import java.util.List;

public class HawkerStall extends BaseDbFields {
    // TODO: Need to add in imageUrl for the hawker stall
    // TODO: Do we want a field to track numReviews only? Thinking of storage space concerns
    String id, address, hawkerCentre, name;
    HashMap<String, String> openingHours;
    List<Review> reviews;
    List<String> reviewsIds;

    public HawkerStall(){};

    public HawkerStall(String address, String name, HashMap<String,String> openingHours, String hawkerCentre, List<String> reviewsIds){

        this.address = address;
        this.name = name;
        this.openingHours = openingHours;
        this.hawkerCentre = hawkerCentre;
        this.reviewsIds = reviewsIds;
    }

    public void attachReviews(List<Review> reviews){
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getHawkerCentre() {
        return hawkerCentre;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getOpeningHours() {
        return openingHours;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<String> getReviewsIds() {
        return reviewsIds;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("address", this.address);
        map.put("openingHours", this.openingHours);
        map.put("name", this.name);
        map.put("reviews", this.reviews);
        map.put("hawkerCentre", this.hawkerCentre);
        return map;
    }


}
