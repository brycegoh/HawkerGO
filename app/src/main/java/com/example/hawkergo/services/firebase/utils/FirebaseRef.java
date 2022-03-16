package com.example.hawkergo.services.firebase.utils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseRef {
    public static CollectionReference getCollectionReference(String name){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(name);
    };

    public static DocumentReference getDocumentReference(String name){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.document(name);
    };
}
