package com.example.hawkergo.services.firebase.repositories;

import androidx.annotation.NonNull;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.interfaces.HawkerStallQueryable;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.example.hawkergo.services.firebase.utils.FirebaseRef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class HawkerStallsRepository implements HawkerStallQueryable {
    private static final String collectionId = FirebaseConstants.CollectionIds.HAWKER_STALLS;
    private static final CollectionReference collectionRef = FirebaseRef.getCollectionReference(collectionId);

    /**
     * Adds hawker stall into hawkerStall collection
     * @param hawkerStall   New hawker stall data to be inserted
     * @param eventHandler  Callback to handle on success or failure events
     */
    public static void addHawkerStall(HawkerStall hawkerStall, String hawkerCentreID, DbEventHandler<String> eventHandler ){
        DocumentReference docRef = collectionRef.document();
//        newHawkerCenterData.updateDates();
        docRef.set(hawkerStall)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        eventHandler.onSuccess(FirebaseConstants.DbResponse.SUCCESS);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        eventHandler.onFailure(e);
                    }
                });
        HawkerCentresRepository.addStallIntoHawkerCentre(
                hawkerCentreID,
                hawkerStall,
                eventHandler
        );
    };

    /**
     * Update a hawker stall by its ID
     * @param hawkerStallID     ID of the hawker stall document
     * @param hawkerStallFields HawkerStall builder object that contains selected fields to update
     * @param eventHandler      Callback to handle on success or failure events
     */
    public static void updateHawkerStallById(String hawkerStallID, HawkerStall hawkerStallFields, DbEventHandler<String> eventHandler){
        // TODO: check if builder pattern is necessary for update function
        DocumentReference documentReference = collectionRef.document(hawkerStallID);
        Map<String, Object> fieldsToUpdate = hawkerStallFields.toMap();
        documentReference.update(fieldsToUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                eventHandler.onSuccess(FirebaseConstants.DbResponse.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
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
                eventHandler.onSuccess(FirebaseConstants.DbResponse.SUCCESS);
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
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
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

}
