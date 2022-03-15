package com.example.hawkergo.services.firebase.interfaces;

import com.example.hawkergo.models.HawkerCentre;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;

public interface HawkerCentreQueryable {
    void insertHawkerCentre(HawkerCentre hawkerStall, QueryDocumentEventHandler eventHandler );
    void updateHawkerCentre(String hawkerCentreID, HashMap<String, Object> hawkerCentre, WriteEventHandler eventHandler);
    void deleteHawkerCentre(String hawkerCentreID,WriteEventHandler callBack);
    void getHawkerCentreByID(String hawkerCentreID, QueryDocumentEventHandler eventHandler);
    ListenerRegistration getAllHawkerCentresAndListenToChanges(QueryMultiDocumentsEventHandler eventHandler);
}
