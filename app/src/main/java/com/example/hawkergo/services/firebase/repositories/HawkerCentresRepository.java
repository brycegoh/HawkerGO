package com.example.hawkergo.services.firebase.repositories;

import androidx.annotation.NonNull;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
import com.example.hawkergo.services.firebase.interfaces.HawkerCentreQueryable;
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

public class HawkerCentresRepository implements HawkerCentreQueryable {
    private static final String collectionId = FirebaseConstants.CollectionIds.HAWKER_CENTRES;
    private static final CollectionReference collectionRef = FirebaseRef.getCollectionReference(collectionId);

    /**
     * Gets all hawker centres
     *
     * @param eventHandler Callback to handle on success or failure events
     */
    public static void getAllHawkerCentres(DbEventHandler<List<HawkerCentre>> eventHandler) {
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        List<HawkerCentre> hawkerCentreList = querySnapshot.toObjects(HawkerCentre.class);
                        eventHandler.onSuccess(hawkerCentreList);
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


    /**
     * Get a hawker centres by its ID
     *
     * @param hawkerCentreID ID of the hawker centre document
     * @param eventHandler   Callback to handle on success or failure events
     */
    public static void getHawkerCentreByID(String hawkerCentreID, DbEventHandler<HawkerCentre> eventHandler) {
        DocumentReference docRef = collectionRef.document(hawkerCentreID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        HawkerCentre hawkerCentre = document.toObject(HawkerCentre.class);
                        eventHandler.onSuccess(hawkerCentre);
                    } else {
                        eventHandler.onSuccess(null);
                    }
                } else {
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    }


    /**
     * Adds hawker centre into hawkerCentre collection
     *
     * @param newHawkerCenterData New hawker centre data to be inserted
     * @param eventHandler        Callback to handle on success or failure events
     */
    public static void addHawkerCentre(HawkerCentre newHawkerCenterData, DbEventHandler<String> eventHandler) {
        DocumentReference docRef = collectionRef.document();
//        newHawkerCenterData.updateDates();
        docRef.set(newHawkerCenterData)
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
    }


    /**
     * Adds hawker centre into hawkerStall collection then
     * updates the hawker centre stalls field with the new hawker stall ID
     *
     * @param hawkerCentreID ID of the hawker centre document
     * @param newHawkerStall New hawker stall to be inserted in hawkerStalls collection
     * @param eventHandler   Callback to handle on success or failure events
     */
    public static void addStallIntoHawkerCentre(String hawkerCentreID, HawkerStall newHawkerStall, DbEventHandler<String> eventHandler) {
        DocumentReference documentReference = collectionRef.document(hawkerCentreID);
        // TODO: add inserting of stall into hawkerstall collection and update dateUpdated
        String hawkerStallId = "test addStallIntoHawkerCentre";

        documentReference
                .update("stallsID", FieldValue.arrayUnion(hawkerStallId))
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
    }

    /**
     * Update a hawker centre by its ID
     *
     * @param hawkerCentreID     ID of the hawker centre document
     * @param hawkerCentreFields HawkerCentre builder object that contains selected fields to update
     * @param eventHandler       Callback to handle on success or failure events
     */
    public static void updateHawkerCentreById(String hawkerCentreID, HawkerCentre.Builder hawkerCentreFields, DbEventHandler<String> eventHandler) {
        DocumentReference documentReference = collectionRef.document(hawkerCentreID);
        Map<String, Object> fieldsToUpdate = hawkerCentreFields.build().toMap();
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
    }


    /**
     * Delete a hawker centre
     *
     * @param hawkerCentreID ID of the hawker centre document
     * @param eventHandler   Callback to handle on success or failure events
     */
    public static void deleteHawkerCentre(String hawkerCentreID, DbEventHandler<String> eventHandler) {
        DocumentReference documentReference = collectionRef.document(hawkerCentreID);

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
    }

//    public static ListenerRegistration getAllHawkerCentresAndListenToChanges(QueryHawkerCentreEventHandler eventHandler) {
//        return null;
//    }
//


//    private static HawkerCentre deserializeData(DocumentSnapshot document){
//        HawkerCentre insertedHawkerCentre = document.toObject(HawkerCentre.class);
//        if (insertedHawkerCentre != null) {
//            insertedHawkerCentre.attachID(document.getId());
//        }
//        return insertedHawkerCentre;
//    }
//
//    private static List<HawkerCentre> deserializeData(QuerySnapshot querySnap){
//        ArrayList<HawkerCentre> hawkerCentreList = new ArrayList<>();
//        List<DocumentSnapshot> documents = querySnap.getDocuments();
//        for(DocumentSnapshot x : documents){
//
//            hawkerCentreList.add( deserializeData(x) );
//        }
//        return hawkerCentreList;
//    }
}
