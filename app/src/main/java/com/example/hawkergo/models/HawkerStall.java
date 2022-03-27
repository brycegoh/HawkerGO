package com.example.hawkergo.models;

import java.util.HashMap;
import java.util.List;

public class HawkerStall extends BaseDbFields {
    public String address, hawkerCentre, name, hawkerCentreID;
    public HashMap<String, String> openingHours;
    public List<Review> reviews;
    public List<String> reviewsIds;

    public HawkerStall(){};

    public HawkerStall(String address, String name, HashMap<String,String> openingHours, String hawkerCentre, List<String> reviewsIds, String hawkerCentreID){

        this.address = address;
        this.name = name;
        this.openingHours = openingHours;
        this.hawkerCentre = hawkerCentre;
        this.reviewsIds = reviewsIds;
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
        map.put("hawkerCentre", this.hawkerCentre);
        map.put("hawkerCentreID", this.hawkerCentreID);
        return map;
    }


}
