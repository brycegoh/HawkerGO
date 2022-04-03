package com.example.hawkergo.models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class BaseDbFields {
    @DocumentId
    private String id;
    // @ServerTimestamp
    // public Date dateCreated;
    // @ServerTimestamp
    // public Date dateUpdated;

    // public void updateDateUpdated(){
    //     this.dateUpdated = new Date();
    // }
    // public void updateDateCreated(){
    //     this.dateCreated = new Date();
    // }
    // public void updateDates(){
    //     updateDateUpdated();
    //     updateDateCreated();
    // }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
