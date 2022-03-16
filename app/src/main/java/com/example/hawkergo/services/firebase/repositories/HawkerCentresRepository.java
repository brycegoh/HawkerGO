package com.example.hawkergo.services.firebase.repositories;

import androidx.annotation.NonNull;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.services.firebase.interfaces.hawkerCentreQueryable;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HawkerCentresRepository implements hawkerCentreQueryable {
    private static HawkerCentresRepository instance;
    private static final String collectionId = FirebaseConstants.CollectionIds.HAWKER_CENTRES;
    private static final CollectionReference collectionRef = FirebaseRef.getCollectionReference(collectionId);
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private HawkerCentresRepository (){};

    public static HawkerCentresRepository getInstance(){
        if(instance == null){
            instance = new HawkerCentresRepository();
        }
        return instance;
    }

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
                                HawkerCentre insertedHawkerCentre = document.toObject(HawkerCentre.class);
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

    @Override
    public void getAllHawkerCentres(QueryHawkerCentreEventHandler eventHandler) {
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



    public void getHawkerCentreByID(String hawkerCentreID, QueryHawkerCentreEventHandler eventHandler) {
        DocumentReference docRef = collectionRef.document(hawkerCentreID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        HawkerCentre hawkerCentre = document.toObject(HawkerCentre.class);
                        ArrayList<HawkerCentre> hawkerCentreList = new ArrayList<>();
                        hawkerCentreList.add(hawkerCentre);
                        eventHandler.onSuccess(hawkerCentreList);
                    }else{
                        eventHandler.onSuccess(null);
                    }
                } else {
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    }




    public ListenerRegistration getAllHawkerCentresAndListenToChanges(QueryHawkerCentreEventHandler eventHandler) {
        return null;
    }
}
