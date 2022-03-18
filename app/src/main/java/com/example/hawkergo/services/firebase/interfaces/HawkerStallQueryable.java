package com.example.hawkergo.services.firebase.interfaces;

import com.example.hawkergo.FOR_REFERENCE.QueryHawkerStallEventHandler;
import com.example.hawkergo.FOR_REFERENCE.WriteEventHandler;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.Review;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Map;

public interface HawkerStallQueryable {
    void addHawkerStall(HawkerStall hawkerStall, QueryHawkerStallEventHandler eventHandler );
    void updateHawkerStallById(String hawkerStallID, Map<String, Object> fieldToUpdate, WriteEventHandler eventHandler);
    void deleteHawkerStall(String hawkerStallID,WriteEventHandler callBack);
    void getHawkerStallByID(String hawkerStallID, QueryHawkerStallEventHandler eventHandler);
    void addReview(String hawkerStallID, Review newReview, WriteEventHandler eventHandler);
    void getAllHawkerStalls(QueryHawkerStallEventHandler eventHandler);
    ListenerRegistration getAllHawkerStallsAndListenToChanges(QueryHawkerStallEventHandler eventHandler);
}
