package com.example.hawkergo.services.interfaces;

import com.example.hawkergo.models.HawkerStall;

import java.util.List;
import java.util.Map;

public interface HawkerStallQueryable {
    static void addHawkerStall(HawkerStall hawkerStall, DbEventHandler<String> eventHandler ){};
    static void updateHawkerStallById(String hawkerStallID, Map<String, Object> fieldToUpdate, DbEventHandler<String> eventHandler){};
    static void deleteHawkerStall(String hawkerStallID,DbEventHandler<String> callBack){};
    static void getHawkerStallByID(String hawkerStallID, DbEventHandler<HawkerStall> eventHandler){};
    static void addStallIntoHawkerCentre(String hawkerCentreID, HawkerStall newHawkerStall, DbEventHandler<String> eventHandler){};
    static void getAllHawkerStalls(DbEventHandler<List<HawkerStall>> eventHandler){};

//    static ListenerRegistration getAllHawkerCentresAndListenToChanges(QueryHawkerCentreEventHandler eventHandler){return null;};
//
//    static HawkerCentre deserializeData(DocumentSnapshot document){return null;};
//
//    static List<HawkerCentre> deserializeData(QuerySnapshot querySnap) {
//        return null;
//    }
}