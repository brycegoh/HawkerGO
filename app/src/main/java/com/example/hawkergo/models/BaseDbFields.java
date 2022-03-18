package com.example.hawkergo.models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class BaseDbFields {
    @DocumentId
    public String id;

    public Date dateCreated; ;
    public Date dateUpdated;

    public void updateDateUpdated(){
        this.dateUpdated = new Date();
    }
    public void updateDateCreated(){
        this.dateCreated = new Date();
    }
    public void updateDates(){
        updateDateUpdated();
        updateDateCreated();
    }
}
