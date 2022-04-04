package com.example.hawkergo.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HawkerCentre extends BaseDbFields implements Searchable {
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

    /**
     * Transform HawkerCentre object to a Map for firestore to update from
     *
     * */
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        // if(this.dateCreated != null) map.put("dateCreated", dateCreated);
        // if(this.dateUpdated != null) map.put("dateUpdated", dateUpdated);
        if(this.address != null) map.put("address", this.address);
        if(this.openingHours != null) map.put("openingHours", this.openingHours);
        if(this.name != null) map.put("name", this.name);
        if(this.stallsID != null) map.put("stallsID", this.stallsID);
        map.put("dateUpdated", new Date());
        return map;
    }

    /**
     * Builder pattern for selectively building of HawkerCentre object
     *
     * To be used in conjunction with toMap method
     * to transform HawkerCentre object to a Map
     * which is then passed to Firebase's update method.
     *
     * */
    public static class Builder {
        private String address, name, id, imageUrl = null;
        private OpeningHours openingHours = null;
        private List<String> stallsID = null;

        public Builder addId(String id) {
            this.id = id;
            return this;
        }

        public Builder addAddress(String address) {
            this.address = address;
            return this;
        }
        public Builder addName(String name) {
            this.name = name;
            return this;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public Builder addOpeningHours(OpeningHours openingHours) {
            this.openingHours = openingHours;
            return this;
        }

        public Builder addImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }
        public Builder addStallsID(List<String> stallsID) {
            this.stallsID = stallsID;
            return this;
        }

        public HawkerCentre build() {
            return new HawkerCentre(address,  name, openingHours, imageUrl, stallsID);
        }
    }

}
