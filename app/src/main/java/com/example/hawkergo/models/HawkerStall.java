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

    public HawkerStall() {};

    public HawkerStall(String address, String name, HashMap<String, String> openingHours, String hawkerCentre, List<String> imageUrl, List<String> reviewsIds) {
        this.address = address;
        this.name = name;
        this.imageUrls = imageUrl;
        this.reviewsIds = reviewsIds;
    }

    public HawkerStall(String address, String name, OpeningHours openingHours, List<String> imageUrl, List<String> popularItems, List<String> tags, String hawkerCentreId) {

        this.address = address;
        this.name = name;
        this.imageUrls = imageUrl;
        this.openingHours = openingHours;
        this.popularItems = popularItems;
        this.tags = tags;
        this.hawkerCentreId = hawkerCentreId;
    }

    public void attachReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("address", this.address);
        map.put("openingHours", this.openingHours);
        map.put("name", this.name);
        map.put("reviews", this.reviews);
        return map;
    }

    public Double getAverageReview(){
        if(this.getReviewCount() == null || this.getTotalRating() == null){
            return null;
        }
        return Math.round((this.getTotalRating() / this.getReviewCount())*100.0)/100.0;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrl) {
        this.imageUrls = imageUrl;
    }

    public String getHawkerCentreId() {
        return hawkerCentreId;
    }

    public void setHawkerCentreId(String hawkerCentreId) {
        this.hawkerCentreId = hawkerCentreId;
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

    public Double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }
}