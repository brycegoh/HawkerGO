package com.example.hawkergo.FOR_REFERENCE;

import com.google.firebase.firestore.DocumentReference;

public interface WriteEventHandler {
    void onSuccess(String o);
    void onFailure(Exception e);
}