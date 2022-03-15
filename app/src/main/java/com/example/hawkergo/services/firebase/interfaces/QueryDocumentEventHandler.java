package com.example.hawkergo.services.firebase.interfaces;

import com.google.firebase.firestore.DocumentSnapshot;

import javax.annotation.Nullable;

public interface QueryDocumentEventHandler {
    void onSuccess(@Nullable DocumentSnapshot o);
    void onFailure(Exception e);
}
