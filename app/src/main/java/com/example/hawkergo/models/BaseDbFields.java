package com.example.hawkergo.models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class BaseDbFields {
    @DocumentId
    private String id;

    public String getId() {
        return id;
    }
}






