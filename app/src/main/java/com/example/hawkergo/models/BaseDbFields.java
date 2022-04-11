package com.example.hawkergo.models;

import com.google.firebase.firestore.DocumentId;

public class BaseDbFields {
    @DocumentId
    private String id;

    public String getId() {
        return id;
    }
}






