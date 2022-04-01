package com.example.hawkergo.services.interfaces;

import com.google.firebase.firestore.FirebaseFirestoreException;

public interface DataChangeEventHandler {
    void onInsert(Object o);
    void onUpdate(Object o);
    void onDelete(Object o);
    void onError(FirebaseFirestoreException o);
}