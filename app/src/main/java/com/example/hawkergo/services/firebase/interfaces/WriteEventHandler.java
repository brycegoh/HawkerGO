package com.example.hawkergo.services.firebase.interfaces;

import com.google.firebase.firestore.DocumentReference;

public interface WriteEventHandler {
    void onSuccess(String o);
    void onFailure(Exception e);
}