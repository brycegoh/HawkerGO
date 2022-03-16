package com.example.hawkergo.services.firebase.interfaces;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

public interface hawkerCentreQueryable {
    void addHawkerCentre(HawkerCentre hawkerStall, QueryHawkerCentreEventHandler eventHandler );
    void updateHawkerCentreById(String hawkerCentreID, Map<String, Object> fieldToUpdate, WriteEventHandler eventHandler);
    void deleteHawkerCentre(String hawkerCentreID,WriteEventHandler callBack);
    void getHawkerCentreByID(String hawkerCentreID, QueryHawkerCentreEventHandler eventHandler);
    void addStallIntoHawkerCentre(String hawkerCentreID, HawkerStall newHawkerStall, WriteEventHandler eventHandler);
    void getAllHawkerCentres(QueryHawkerCentreEventHandler eventHandler);
    ListenerRegistration getAllHawkerCentresAndListenToChanges(QueryHawkerCentreEventHandler eventHandler);
}
