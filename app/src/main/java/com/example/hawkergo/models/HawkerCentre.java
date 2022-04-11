package com.example.hawkergo.models;

import java.util.List;

public class HawkerCentre extends BaseDbFields {
    private String address, name, imageUrl;
    private OpeningHours openingHours;
    private  List<String> stallsID;
    private List<String> tags;

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

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public List<String> getStallsID() {
        return stallsID;
    }

    public void setStallsID(List<String> stallsID) {
        this.stallsID = stallsID;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public  HawkerCentre(){};

    public HawkerCentre(String address, String name, OpeningHours openingHours, String imageUrl, List<String> stallsID) {
        this.address = address;
        this.name = name;
        this.openingHours = openingHours;
        this.imageUrl = imageUrl;
        this.stallsID = stallsID;
    }

}
