package com.example.hawkergo.services.firebase.utils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseCollectionRef {
    public static CollectionReference getReference(String name){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(name);
    };
}
