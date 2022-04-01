package com.example.hawkergo.services.firebase.repositories;

import androidx.annotation.NonNull;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.Tags;
import com.example.hawkergo.services.firebase.interfaces.DbEventHandler;
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

public class TagsService {
    private static final String collectionId = FirebaseConstants.CollectionIds.TAGS;
    private static final CollectionReference collectionRef = FirebaseRef.getCollectionReference(collectionId);
    private static final String tagDocumentId = "PZQY0RGoRhiGvSSQsnFP";

    /**
     * Gets all tags
     *
     * @param eventHandler Callback to handle on success or failure events
     */
    public static void getAllTags(DbEventHandler<Tags> eventHandler) {
        DocumentReference docRef = collectionRef.document(tagDocumentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Tags tags = document.toObject(Tags.class);
                        eventHandler.onSuccess(tags);
                    } else {
                        eventHandler.onSuccess(null);
                    }
                } else {
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    }

    public static void addTag(String tag, DbEventHandler<String> eventHandler){
        DocumentReference docRef = collectionRef.document(tagDocumentId);
        docRef
                .update("categories", FieldValue.arrayUnion(tag))
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
}