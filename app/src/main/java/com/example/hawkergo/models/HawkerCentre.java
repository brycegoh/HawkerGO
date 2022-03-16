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

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getOpeningHours() {
        return openingHours;
    }

    public List<String> getStallsID() {
        return stallsID;
    }

    public String getId() {
        return id;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("address", getAddress());
        map.put("openingHours", getOpeningHours());
        map.put("name", getName());
        map.put("stallsID", getStallsID());
        return map;
    }


}
