package com.example.hawkergo.services.firebase.repositories;

import androidx.annotation.NonNull;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.services.firebase.interfaces.HawkerCentreQueryable;
import com.example.hawkergo.services.firebase.interfaces.QueryHawkerCentreEventHandler;
import com.example.hawkergo.services.firebase.interfaces.WriteEventHandler;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HawkerCentresRepository implements HawkerCentreQueryable {
    private static HawkerCentresRepository instance;
    private static final String collectionId = FirebaseConstants.CollectionIds.HAWKER_CENTRES;
    private static final CollectionReference collectionRef = FirebaseRef.getCollectionReference(collectionId);
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private HawkerCentresRepository() {
    }

    ;

    public static HawkerCentresRepository getInstance() {
        if (instance == null) {
            instance = new HawkerCentresRepository();
        }
        return instance;
    }


    /**
     * Gets all hawker centres
     *
     * @param eventHandler      Callback to handle on success or failure events
     */
    @Override
    public void getAllHawkerCentres(QueryHawkerCentreEventHandler eventHandler) {
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        List<HawkerCentre> hawkerCentreList = deserializeData(querySnapshot);
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
     * @param hawkerCentreID    ID of the hawker centre document
     * @param eventHandler      Callback to handle on success or failure events
     */
    public void getHawkerCentreByID(String hawkerCentreID, QueryHawkerCentreEventHandler eventHandler) {
        DocumentReference docRef = collectionRef.document(hawkerCentreID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        HawkerCentre hawkerCentre = deserializeData(document);
                        ArrayList<HawkerCentre> hawkerCentreList = new ArrayList<>();
                        hawkerCentreList.add(hawkerCentre);
                        eventHandler.onSuccess(hawkerCentreList);
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
     * Adds hawker centre into hawkerCentre collection then gets the inserted hawker centre
     *
     * @param newHawkerCenterData       New hawker centre data to be inserted
     * @param eventHandler              Callback to handle on success or failure events
     */
    public void addHawkerCentre(HawkerCentre newHawkerCenterData, QueryHawkerCentreEventHandler eventHandler) {
        collectionRef
                .add(newHawkerCenterData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Task<DocumentSnapshot> task = documentReference.get();
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                HawkerCentre insertedHawkerCentre = deserializeData(document);
                                ArrayList<HawkerCentre> hawkerCentreList = new ArrayList<>();
                                hawkerCentreList.add(insertedHawkerCentre);
                                eventHandler.onSuccess(hawkerCentreList);
                            } else {
                                eventHandler.onSuccess(null);
                            }
                        } else {
                            eventHandler.onFailure(task.getException());
                        }
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
     * @param hawkerCentreID       ID of the hawker centre document
     * @param newHawkerStall       New hawker stall to be inserted in hawkerStalls collection
     * @param eventHandler         Callback to handle on success or failure events
     */
    public void addStallIntoHawkerCentre(String hawkerCentreID, HawkerStall newHawkerStall, WriteEventHandler eventHandler) {
        DocumentReference documentReference = collectionRef.document(hawkerCentreID);
        // TODO: add inserting of stall into hawkerstall collection
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
     * @param hawkerCentreID       ID of the hawker centre document
     * @param fieldsToUpdate       Map of fields to be updated
     * @param eventHandler         Callback to handle on success or failure events
     */
    public void updateHawkerCentreById(String hawkerCentreID, Map<String, Object> fieldsToUpdate, WriteEventHandler eventHandler) {
        DocumentReference documentReference = collectionRef.document(hawkerCentreID);
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
     * @param hawkerCentreID       ID of the hawker centre document
     * @param eventHandler         Callback to handle on success or failure events
     */
    public void deleteHawkerCentre(String hawkerCentreID, WriteEventHandler eventHandler) {
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


    public ListenerRegistration getAllHawkerCentresAndListenToChanges(QueryHawkerCentreEventHandler eventHandler) {
        return null;
    }


    private HawkerCentre deserializeData(DocumentSnapshot document){
        HawkerCentre insertedHawkerCentre = document.toObject(HawkerCentre.class);
        if (insertedHawkerCentre != null) {
            insertedHawkerCentre.attachID(document.getId());
        }
        return insertedHawkerCentre;
    }

    private List<HawkerCentre> deserializeData(QuerySnapshot querySnap){
        ArrayList<HawkerCentre> hawkerCentreList = new ArrayList<>();
        List<DocumentSnapshot> documents = querySnap.getDocuments();
        for(DocumentSnapshot x : documents){
            hawkerCentreList.add( deserializeData(x) );
        }
        return hawkerCentreList;
    }
}
