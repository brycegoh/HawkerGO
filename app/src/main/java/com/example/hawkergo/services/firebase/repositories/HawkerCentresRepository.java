package com.example.hawkergo.services.firebase.repositories;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.services.firebase.interfaces.HawkerCentreQueryable;
import com.example.hawkergo.services.firebase.interfaces.QueryDocumentEventHandler;
import com.example.hawkergo.services.firebase.interfaces.QueryMultiDocumentsEventHandler;
import com.example.hawkergo.services.firebase.interfaces.WriteEventHandler;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;

public class HawkerCentresRepository extends FirebaseClient implements HawkerCentreQueryable {

    @Override
    public void insertHawkerCentre(HawkerCentre hawkerStall, QueryDocumentEventHandler eventHandler) {

    }

    @Override
    public void updateHawkerCentre(String hawkerCentreID, HashMap<String, Object> hawkerCentre, WriteEventHandler eventHandler) {

    }

    @Override
    public void deleteHawkerCentre(String hawkerCentreID, WriteEventHandler callBack) {

    }

    @Override
    public void getHawkerCentreByID(String hawkerCentreID, QueryDocumentEventHandler eventHandler) {

    }

    @Override
    public ListenerRegistration getAllHawkerCentresAndListenToChanges(QueryMultiDocumentsEventHandler eventHandler) {
        return null;
    }
}
