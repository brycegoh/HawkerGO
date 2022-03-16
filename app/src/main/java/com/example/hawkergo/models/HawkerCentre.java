package com.example.hawkergo.models;

import java.util.HashMap;
import java.util.List;

public class HawkerCentre {
    String address, name, id;
    HashMap<String, String> openingHours;
    List<String> stallsID;

    public HawkerCentre() {
    }

    public HawkerCentre(String address, String name, HashMap<String, String> openingHours, List<String> stallsID) {
        this.address = address;
        this.name = name;
        this.openingHours = openingHours;
        this.stallsID = stallsID;
    }

    /**
     * Transform object to a map for firestore to update from
     *
     * */
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        if(this.id != null) map.put("id", this.id);
        if(this.address != null) map.put("address", this.address);
        if(this.openingHours != null) map.put("openingHours", this.openingHours);
        if(this.name != null) map.put("name", this.name);
        if(this.stallsID != null) map.put("stallsID", this.stallsID);
        return map;
    }

    public void attachID(String id){
        this.id = id;
    }

    /**
     * Builder pattern for building of HawkerCentre object
     * to be used to selectively update hawker centre documents
     *
     * */
    public static class Builder {
        private String address, name = null;
        private HashMap<String, String> openingHours = null;
        private List<String> stallsID = null;

        public Builder addAddress(String address) {
            this.address = address;
            return this;
        }
        public Builder addName(String name) {
            this.name = name;
            return this;
        }
        public Builder addOpeningHours(HashMap<String, String> openingHours) {
            this.openingHours = openingHours;
            return this;
        }
        public Builder addStallsID(List<String> stallsID) {
            this.stallsID = stallsID;
            return this;
        }

        public HawkerCentre build() {
            return new HawkerCentre(address,  name, openingHours,  stallsID);
        }
    }

}
