package com.example.hawkergo.services.firebase.interfaces;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public interface QueryCollectionEventHandler {
    void onSuccess(QuerySnapshot o);
    void onFailure(Exception e);
}
