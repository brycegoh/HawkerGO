package com.example.hawkergo.models;

import com.google.firebase.firestore.CollectionReference;

import java.util.Date;

public class BaseDbFields {
    public Date dateCreated; ;
    public Date dateUpdated;
    public String id;

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

    public String getAndAttachId(CollectionReference collectionRef){
        String id = collectionRef.document().getId();
        this.id = id;
        return id;
    }
}
