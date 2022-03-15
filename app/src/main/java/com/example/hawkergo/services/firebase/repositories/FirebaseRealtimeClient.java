package com.example.hawkergo.services.firebase.repositories;

import androidx.annotation.Nullable;

import com.example.hawkergo.services.firebase.interfaces.DataChangeEventHandler;
import com.example.hawkergo.services.firebase.interfaces.QueryMultiDocumentsEventHandler;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

class FirebaseRealtimeClient {


    /**
     * Listen to multiple documents in a collection
     *
     * @param query         query reference to fetch data
     * @param eventHandler  callback for event handling
     *
     * @return listener object so that can remove once exit activity
     */
    protected ListenerRegistration addDocumentListener (Query query, QueryMultiDocumentsEventHandler eventHandler) {
        return query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    eventHandler.onFailure(e);
                }
                eventHandler.onSuccess(value);
            }
        });
    }

    /**
     * View changes between snapshots
     *
     * @param query    to add childEvent listener
     * @param eventHandler callback for event handling
     *
     * @return listener object so that can remove once exit activity
     */
    protected ListenerRegistration addFilterDocumentsListener(final Query query, final DataChangeEventHandler eventHandler) {
        return query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null || snapshots == null || snapshots.isEmpty()) {
                    eventHandler.onError(e);
                    return;
                }
                for (DocumentChange documentChange : snapshots.getDocumentChanges()) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            eventHandler.onInsert(documentChange.getDocument());
                            break;
                        case MODIFIED:
                            eventHandler.onUpdate(documentChange.getDocument());
                            break;
                        case REMOVED:
                            eventHandler.onDelete(documentChange.getDocument());
                            break;
                    }
                }
            }
        });
    }


}



