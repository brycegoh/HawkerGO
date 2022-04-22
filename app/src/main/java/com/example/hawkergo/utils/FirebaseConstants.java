package com.example.hawkergo.utils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 *  this class will define firebase helper functions as well as table names and standard db response
 * */
public class FirebaseConstants
{
    public static class CollectionIds {
        public static String HAWKER_CENTRES = "hawkerCentres";
        public static String HAWKER_STALLS = "hawkerStalls";
        public static String TAGS = "tags";
        public static String REVIEWS = "reviews";
    }


    public static class DbResponse {
        public static String SUCCESS = "success";
    }

    public static CollectionReference getCollectionReference(String name){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(name);
    };

    public static DocumentReference getDocumentReference(String name){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.document(name);
    };

}
