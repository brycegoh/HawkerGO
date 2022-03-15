package com.example.hawkergo.services.firebase.repositories;

import androidx.annotation.NonNull;

import com.example.hawkergo.services.firebase.interfaces.QueryCollectionEventHandler;
import com.example.hawkergo.services.firebase.interfaces.QueryDocumentEventHandler;
import com.example.hawkergo.services.firebase.interfaces.WriteEventHandler;
import com.example.hawkergo.services.firebase.utils.FirebaseConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

class FirebaseQueryClient {


    /**
     * Insert data into Firestore
     *
     * @param documentReference Ref of document in which data is to added into
     * @param dataModel         Model of data to insert into Document
     * @param eventHandler      Event handler containing callbacks
     */
    protected void insertOrOverwrite(DocumentReference documentReference, Object dataModel, WriteEventHandler eventHandler) {
        documentReference.set(dataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                eventHandler.onSuccess(FirebaseConstants.QueryResponse.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventHandler.onFailure(e);
            }
        });
    }

    /**
     * Update data to FireStore
     *
     * @param documentReference Ref of document in which data is to added into
     * @param fieldsToUpdate    Hashmap of fields to update
     * @param eventHandler      Event handler containing callbacks
     */
    protected final void update(final DocumentReference documentReference, final Map<String, Object> fieldsToUpdate, final WriteEventHandler eventHandler) {
        documentReference.update(fieldsToUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                eventHandler.onSuccess(FirebaseConstants.QueryResponse.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventHandler.onFailure(e);
            }
        });
    }


    /**
     * FireStore Create or Merge
     *
     * @param documentReference Ref of document in which data is to added into
     * @param dataModel         Model of data to insert into Document
     * @param eventHandler      Event handler containing callbacks
     */
    protected final void insertOrMerge(final DocumentReference documentReference, final Object dataModel, final WriteEventHandler eventHandler) {
        documentReference.set(dataModel, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                eventHandler.onSuccess(FirebaseConstants.QueryResponse.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventHandler.onFailure(e);
            }
        });
    }


    /**
     * Delete data from FireStore
     *
     * @param documentReference Ref of document in which data is to added into
     * @param eventHandler      Event handler containing callbacks
     */
    protected final void delete(final DocumentReference documentReference, final WriteEventHandler eventHandler) {
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                eventHandler.onSuccess(FirebaseConstants.QueryResponse.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                eventHandler.onFailure(e);
            }
        });
    }


    /**
     * One time data fetch from FireStore with Document reference
     *
     * @param documentReference query of Document reference to fetch data
     * @param eventHandler          callback for event handling
     */
    protected final void getDocument(final DocumentReference documentReference, final QueryDocumentEventHandler eventHandler) {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        eventHandler.onSuccess(document);
                    }else{
                        eventHandler.onSuccess(null);
                    }
                } else {
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    }


    /**
     * One time data fetch from FireStore with Query reference
     *
     * @param query         query reference to fetch data
     * @param eventHandler  callback for event handling
     */
    protected final void filterDocuments(final Query query, final QueryCollectionEventHandler eventHandler) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        eventHandler.onSuccess(querySnapshot);
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
     * One time data fetch from FireStore with Query reference
     *
     * @param query         query reference to fetch data
     * @param eventHandler  callback for event handling
     */
    protected final void getDocuments(final Query query, final QueryCollectionEventHandler eventHandler) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        eventHandler.onSuccess(querySnapshot);
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

