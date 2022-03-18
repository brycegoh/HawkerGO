package com.example.hawkergo.services.firebase.utils;

import com.example.hawkergo.models.BaseDbFields;
import com.google.firebase.firestore.CollectionReference;

import java.util.Date;

public class FirebaseHelper {
    public static void updateDateUpdated(BaseDbFields x){
        x.dateUpdated = new Date();
    }
    public static void updateDateCreated(BaseDbFields x){
        x.dateCreated = new Date();
    }
    public static void updateDates(BaseDbFields x){
        updateDateUpdated(x);
        updateDateCreated(x);
    }
    public static String getAndAttachId(BaseDbFields x, CollectionReference collectionRef){
        String id = collectionRef.document().getId();
        x.id = id;
        return id;
    }
}
