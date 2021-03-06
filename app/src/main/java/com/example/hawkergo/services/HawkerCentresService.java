package com.example.hawkergo.services;

import androidx.annotation.NonNull;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.services.interfaces.HawkerCentreQueryable;
import com.example.hawkergo.utils.FirebaseConstants;
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

public class HawkerCentresService implements HawkerCentreQueryable {
    private static final String collectionId = FirebaseConstants.CollectionIds.HAWKER_CENTRES;
    private static final CollectionReference collectionRef = FirebaseConstants.getCollectionReference(collectionId);

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
                    if (querySnapshot != null) {
                        List<HawkerCentre> hawkerCentreList = querySnapshot.toObjects(HawkerCentre.class);
                        eventHandler.onSuccess(hawkerCentreList);
                    }
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
     * Gets all hawker centres based on a search string
     *
     * @param searchTerm user inputted String to search through hawker centre based on name
     * @param eventHandler Callback to handle on success or failure events
     */
    public static void searchAllHawkerCentres(String searchTerm, DbEventHandler<List<HawkerCentre>> eventHandler) {
        collectionRef
                .whereGreaterThanOrEqualTo("name", searchTerm)
                .whereLessThanOrEqualTo("name", searchTerm + "\uF7FF")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
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
        HawkerStallsService.addHawkerStall(
                newHawkerStall,
                hawkerCentreID,
                new DbEventHandler<String>() {
                    @Override
                    public void onSuccess(String hawkerStallId) {
                        HashMap<String, Object> updateFields  = new HashMap<>();
                        updateFields.put("stallsID", FieldValue.arrayUnion(hawkerStallId));
                        for(String s : newHawkerStall.getTags()){
                            updateFields.put("tags", FieldValue.arrayUnion(s));
                        }

                        documentReference.update(updateFields)
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
                    @Override
                    public void onFailure(Exception e) {
                        eventHandler.onFailure(e);
                    }
                }
        );
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

    /**
     *  Get HawkerCentre collection Reference
     *
     * @return CollectionReference of HawkerCentre Collection
     * */
    public static CollectionReference getCollectionRef() {
        return collectionRef;
    }


    /**
     * Filter hawker centres based on their categories
     *
     * @param query          Firebase Query
     * @param eventHandler   Callback to handle on success or failure events
     */
    public static void filterHawkerCentre(Query query , DbEventHandler<List<HawkerCentre>> eventHandler) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
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
}
