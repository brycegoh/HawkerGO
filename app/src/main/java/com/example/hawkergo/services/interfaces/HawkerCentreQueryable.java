package com.example.hawkergo.services.interfaces;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;

import java.util.List;
import java.util.Map;

public interface HawkerCentreQueryable {
    static void addHawkerCentre(HawkerCentre hawkerCentre, DbEventHandler<String> eventHandler ){};
    static void updateHawkerCentreById(String hawkerCentreID, Map<String, Object> fieldToUpdate, DbEventHandler<String> eventHandler){};
    static void deleteHawkerCentre(String hawkerCentreID,DbEventHandler<String> callBack){};
    static void getHawkerCentreByID(String hawkerCentreID, DbEventHandler<HawkerCentre> eventHandler){};
    static void addStallIntoHawkerCentre(String hawkerCentreID, HawkerStall newHawkerStall, DbEventHandler<String> eventHandler){};
    static void getAllHawkerCentres(DbEventHandler<List<HawkerCentre>> eventHandler){};
//    static ListenerRegistration getAllHawkerCentresAndListenToChanges(QueryHawkerCentreEventHandler eventHandler){return null;};
//
//    static HawkerCentre deserializeData(DocumentSnapshot document){return null;};
//
//    static List<HawkerCentre> deserializeData(QuerySnapshot querySnap) {
//        return null;
//    }
}
