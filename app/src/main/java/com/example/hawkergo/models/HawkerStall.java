package com.example.hawkergo.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HawkerStall extends BaseDbFields {
    // TODO: Need to add in imageUrl for the hawker stall
    // TODO: Do we want a field to track numReviews only? Thinking of storage space concerns
    private String address , name, imageUrl, hawkerCentreID;
    private Integer reviewCount;
    private OpeningHours openingHours;
    private List<Review> reviews;
    private List<String> reviewsIds;
    private List<String> popularItems;
    private List<String> tags;

    public HawkerStall(){};

    public HawkerStall(String address, String name, HashMap<String,String> openingHours, String hawkerCentre, String imageUrl, List<String> reviewsIds){
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
        map.put("address", this.address);
        map.put("openingHours", this.openingHours);
        map.put("name", this.name);
        map.put("reviews", this.reviews);
        return map;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHawkerCentreID() {
        return hawkerCentreID;
    }

    public void setHawkerCentreID(String hawkerCentreID) {
        this.hawkerCentreID = hawkerCentreID;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<String> getReviewsIds() {
        return reviewsIds;
    }

    public void setReviewsIds(List<String> reviewsIds) {
        this.reviewsIds = reviewsIds;
    }

    public List<String> getPopularItems() {
        return popularItems;
    }

    public void setPopularItems(List<String> popularItems) {
        this.popularItems = popularItems;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}