package com.example.hawkergo.services;

import androidx.annotation.NonNull;

import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.services.interfaces.HawkerStallQueryable;
import com.example.hawkergo.services.utils.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HawkerStallsService implements HawkerStallQueryable {
    private static final String collectionId = FirebaseHelper.CollectionIds.HAWKER_STALLS;
    private static final CollectionReference collectionRef = FirebaseHelper.getCollectionReference(collectionId);

    /**
     * Adds hawker stall into hawkerStall collection
     * @param hawkerStall   New hawker stall data to be inserted
     * @param eventHandler  Callback to handle on success or failure events
     */
    public static void addHawkerStall(HawkerStall hawkerStall, String hawkerCentreID, DbEventHandler<String> eventHandler ){
        DocumentReference docRef = collectionRef.document();
        String docId = docRef.getId();
        docRef.set(hawkerStall)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        eventHandler.onSuccess(docId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        eventHandler.onFailure(e);
                    }
                });
    };

    /**
     * Delete a hawker stall
     * @param hawkerStallID ID of the hawker stall document
     * @param eventHandler  Callback to handle on success or failure events
     */
    public static void deleteHawkerStall(String hawkerStallID,DbEventHandler<String> eventHandler){
        DocumentReference documentReference = collectionRef.document(hawkerStallID);

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                eventHandler.onSuccess(FirebaseHelper.DbResponse.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventHandler.onFailure(e);
            }
        });
    };

    /**
     * Get a hawker stall by its ID
     * @param hawkerStallID ID of the hawker centre document
     * @param eventHandler  Callback to handle on success or failure events
     */
    public static void getHawkerStallByID(String hawkerStallID, DbEventHandler<HawkerStall> eventHandler){
        System.out.println("===========");
        System.out.println(hawkerStallID);
        DocumentReference docRef = collectionRef.document(hawkerStallID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        HawkerStall hawkerStall = document.toObject(HawkerStall.class);
                        eventHandler.onSuccess(hawkerStall);
                    } else {
                        eventHandler.onSuccess(null);
                    }
                } else {
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    };

    /**
     * Gets all hawker stalls
     * @param eventHandler Callback to handle on success or failure events
     */
    public static void getAllHawkerStalls(DbEventHandler<List<HawkerStall>> eventHandler){
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        List<HawkerStall> hawkerStallsList = querySnapshot.toObjects(HawkerStall.class);
                        eventHandler.onSuccess(hawkerStallsList);
                    } else {
                        eventHandler.onSuccess(null);
                    }
                } else {
                    eventHandler.onFailure(task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventHandler.onFailure(e);
            }
        });
    };

    public static void filterHawkerCentre(Query query , DbEventHandler<List<HawkerStall>> eventHandler) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        List<HawkerStall> hawkerStallList = querySnapshot.toObjects(HawkerStall.class);
                        eventHandler.onSuccess(hawkerStallList);
                    } else {
                        eventHandler.onSuccess(null);
                    }
                } else {
                    eventHandler.onFailure(task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventHandler.onFailure(e);
            }
        });
    }

    public static void incrementReviewAndAddPhotoCount(String hawkerStallId, Double rating, String selectedImage, DbEventHandler<String> eventHandler){
        Map<String, Object> fieldToUpdate = new HashMap<>();
        fieldToUpdate.put("reviewCount", FieldValue.increment(1));
        fieldToUpdate.put("totalRating", FieldValue.increment(rating));
        fieldToUpdate.put("imageUrls", FieldValue.arrayUnion(selectedImage));
        collectionRef.document(hawkerStallId).update(fieldToUpdate).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        eventHandler.onSuccess(FirebaseHelper.DbResponse.SUCCESS);
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        eventHandler.onFailure(e);
                    }
                }
        );
    }

}
