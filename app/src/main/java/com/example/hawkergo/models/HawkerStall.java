package com.example.hawkergo.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HawkerStall extends BaseDbFields {
    // TODO: Do we want a field to track numReviews only? Thinking of storage space concerns
    public String address , name, imageUrl, hawkerCentreId;
    public OpeningHours openingHours;
    public List<Review> reviews;
    public List<String> reviewsIds;
    public List<String> popularItems;
    public List<String> tags;

    public HawkerStall(){};

    public HawkerStall(String id, String address, String name, HashMap<String,String> openingHours, String hawkerCentre,String hawkerCentreId, String imageUrl, List<String> reviewsIds){
        this.id = id;
        this.address = address;
        this.name = name;
        this.imageUrl = imageUrl;
        this.reviewsIds = reviewsIds;
        this.hawkerCentreId = hawkerCentreId;
    }

    public HawkerStall(String address, String name, OpeningHours openingHours, String imageUrl, List<String> popularItems, List<String> tags){
        System.out.println(openingHours.hours);
        System.out.println(openingHours.remarks);
        System.out.println(openingHours.days);
        System.out.println(popularItems.toString());
        System.out.println(tags.toString());

        this.address = address;
        this.name = name;
        this.imageUrl =imageUrl;
        this.openingHours = openingHours;
        this.imageUrl = imageUrl;
        this.popularItems = popularItems;
        this.tags = tags;
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