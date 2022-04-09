package com.example.hawkergo.models;

import java.util.HashMap;
import java.util.List;

public class HawkerStall extends BaseDbFields {
    private String address;
    private String name;
    private String hawkerCentreId;
    private List<String> imageUrls;
    private Integer reviewCount = 0;
    private Double totalRating = 0.0;
    private OpeningHours openingHours;
    private List<Review> reviews;
    private List<String> reviewsIds;
    private List<String> popularItems;
    private List<String> tags;

    public Integer getReviewCount() {
        return reviewCount;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getHawkerCentreId() {
        return hawkerCentreId;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<String> getReviewsIds() {
        return reviewsIds;
    }

    public List<String> getPopularItems() {
        return popularItems;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Double getTotalRating() {
        return totalRating;
    }


    public HawkerStall() {};

    public HawkerStall(String address, String name, OpeningHours openingHours, List<String> imageUrl, List<String> popularItems, List<String> tags, String hawkerCentreId) {

        this.address = address;
        this.name = name;
        this.imageUrls = imageUrl;
        this.openingHours = openingHours;
        this.popularItems = popularItems;
        this.tags = tags;
        this.hawkerCentreId = hawkerCentreId;
    }

    public Double getAverageReview(){
        if(this.getReviewCount() == null || this.getTotalRating() == null){
            return null;
        }
        return Math.round((this.getTotalRating() / this.getReviewCount())*100.0)/100.0;
    }
}